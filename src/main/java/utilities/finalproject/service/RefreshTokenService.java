package utilities.finalproject.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import utilities.finalproject.domain.RefreshToken;
import utilities.finalproject.domain.User;
import utilities.finalproject.repository.RefreshTokenRepository;
import utilities.finalproject.request.RefreshTokenRequest;
import utilities.finalproject.security.JwtService;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${jwt.refreshExpirationDateInMs}")
    private Long refreshTokenExpirationInMs;
    private UserDetailsServiceImpl userService;
    private RefreshTokenRepository refreshTokenRepository;
    private JwtService jwtService;


    public RefreshTokenService(UserDetailsServiceImpl userService, RefreshTokenRepository refreshTokenRepository,
                               JwtService jwtService) {
        this.userService = userService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    public RefreshToken generateRefreshToken(Long userId) {
        // this method will generate a refresh token for a user
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isPresent()) {
            Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findById(userOpt.get().getId());
            RefreshToken refreshToken = null;
            if (refreshTokenOpt.isPresent()) {
                refreshToken = refreshTokenOpt.get();
                refreshToken.setExpirationDate(new Date(System.currentTimeMillis() + refreshTokenExpirationInMs));
                refreshToken.setRefreshToken(UUID.randomUUID().toString());
            } else {
                refreshToken = new RefreshToken(userOpt.get(), UUID.randomUUID().toString(), new Date(System.currentTimeMillis() + refreshTokenExpirationInMs));
            }
            RefreshToken refreshTokenSaved = refreshTokenRepository.save(refreshToken);
            return refreshToken;
        } else {
            return null;
        }
    }

    public String generateAccessToken(RefreshToken refreshToken) {
        //this method will generate a new access token once the current one expires
        String accessToken = "";
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByRefreshToken(refreshToken.getRefreshToken());
        if (refreshTokenOpt.isPresent()) {
            if (refreshTokenOpt.get().getExpirationDate().after(new Date())) {
                accessToken = jwtService.generateJwtToken(new HashMap<>(), refreshTokenOpt.get().getUser());
            } else {
                throw new IllegalArgumentException("Refresh token expired");
            }
        } else {
            throw new IllegalArgumentException("Refresh token not found");
        }
        return accessToken;
    }
}

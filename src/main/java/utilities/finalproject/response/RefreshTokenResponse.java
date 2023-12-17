package utilities.finalproject.response;

public record RefreshTokenResponse(
        String accessToken,
        String refreshToken

) {}

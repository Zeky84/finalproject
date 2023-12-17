package utilities.finalproject.response;

public record AuthenticationResponse(
        /**
         * It has the token and the username, but can include the role, the user created date, etc.
         */

        String token,
        String refreshToken,
        String username
) {
}

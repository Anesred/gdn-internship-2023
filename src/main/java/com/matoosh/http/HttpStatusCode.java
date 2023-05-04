package com.matoosh.http;

public enum HttpStatusCode {
//    CLIENT ERRORS
    CLIENT_ERROR_400_BAD_REQUEST(400, "Bad Request"),
    CLIENT_ERROR_404_NOT_FOUND(404, "Not Found"),
//    SERVER ERRORS
    SERVER_ERROR_500_INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    SERVER_ERROR_501_NOT_IMPLEMENTED(501, "Not Implemented"),
    SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED(505, "Http Version Not Supported");

    public final int STATUSCODE;
    public final String MESSAGE;

    HttpStatusCode(int STATUSCODE, String MESSAGE) {
        this.STATUSCODE = STATUSCODE;
        this.MESSAGE = MESSAGE;
    }
}

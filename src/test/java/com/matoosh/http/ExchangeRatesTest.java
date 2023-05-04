package com.matoosh.http;

import com.matoosh.httpserver.controllers.ExchangeRates;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ExchangeRatesTest {
    private String apiUrl = "http://api.nbp.pl/api/exchangerates/rates/";

    @Mock
    private ExchangeRates exchangeRates;

    @Test
    void callAverageRatesValidUrl() throws IOException, HttpParsingException {
        String url = "average/gbp/2012-12-12";
        String[] pathParts = url.split("/");
        String expectedResponse = "Currency: funt szterling\n" +
                "Code: GBP\n" +
                "Date: 2012-12-12\n" +
                "Value: 5.0765";

        Mockito.when(exchangeRates.getAverageExchangeRate(apiUrl, pathParts)).thenReturn(expectedResponse);

        String response = exchangeRates.getAverageExchangeRate(apiUrl, pathParts);
        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }

    @Test
    void callAverageRatesInvalidUrl() throws HttpParsingException, IOException {
        String url = "aver/gbp/2012-12-12";
        String[] pathParts = url.split("/");

        Mockito.when(exchangeRates.getAverageExchangeRate(apiUrl, pathParts)).thenThrow(new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST));

        HttpParsingException exception = assertThrows(HttpParsingException.class, () -> exchangeRates.getAverageExchangeRate(apiUrl, pathParts));
        assertEquals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, exception.getErrorCode());
    }

    @Test
    void callMinMaxAverageRatesValidUrl() throws IOException, HttpParsingException {
        String url = "minmaxaverage/aud/last/123";
        String[] pathParts = url.split("/");
        String expectedResponse = "Currency: dolar australijski\n" +
                "Code: AUD\n" +
                "Min Date: 2023-04-26\n" +
                "Min Value: 2.7448\n" +
                "Max Date: 2023-02-13\n" +
                "Max Value: 3.1041";

        Mockito.when(exchangeRates.getMinMaxAverageValue(apiUrl, pathParts)).thenReturn(expectedResponse);

        String response = exchangeRates.getMinMaxAverageValue(apiUrl, pathParts);
        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }

    @Test
    void callMinMaxAverageInvalidUrl() throws HttpParsingException, IOException {
        String url = "minmaxaverage/usd/st/195";
        String[] pathParts = url.split("/");

        Mockito.when(exchangeRates.getMinMaxAverageValue(apiUrl, pathParts)).thenThrow(new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST));

        HttpParsingException exception = assertThrows(HttpParsingException.class, () -> exchangeRates.getMinMaxAverageValue(apiUrl, pathParts));
        assertEquals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, exception.getErrorCode());
    }

    @Test
    void callMajorDifferenceValidUrl() throws IOException, HttpParsingException {
        String url = "majordifference/chf/last/154";
        String[] pathParts = url.split("/");
        String expectedResponse = "Major difference\n" +
                "Currency: frank szwajcarski\n" +
                "Code: CHF\n" +
                "Date: 2022-09-29\n" +
                "Bid Value: 5.0403\n" +
                "Ask Value: 5.1421\n" +
                "Difference: 0.10179999999999989";

        Mockito.when(exchangeRates.getMajorDifference(apiUrl, pathParts)).thenReturn(expectedResponse);

        String response = exchangeRates.getMajorDifference(apiUrl, pathParts);
        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }

    @Test
    void callMajorDifferenceInvalidUrl() throws HttpParsingException, IOException {
        String url = "majordiffernce/usd/lst/195";
        String[] pathParts = url.split("/");

        Mockito.when(exchangeRates.getMajorDifference(apiUrl, pathParts)).thenThrow(new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST));

        HttpParsingException exception = assertThrows(HttpParsingException.class, () -> exchangeRates.getMajorDifference(apiUrl, pathParts));
        assertEquals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, exception.getErrorCode());
    }
}
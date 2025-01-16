package api.freecurrency;

import api.CurrencyAPI;
import api.freecurrency.adapters.CurrencyAdapter;
import api.freecurrency.adapters.ExchangeRateAdapter;
import api.freecurrency.pojos.CurrencyAPIResponse;

import api.freecurrency.pojos.ExchangeRateAPIResponse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import model.Currency;
import model.ExchangeRate;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class FreeCurrencyAPI implements CurrencyAPI {
    public static final String API_ENDPOINT = "https://api.freecurrencyapi.com/v1/";
    private static final String API_KEY = "fca_live_uRhO9OGqZBbn8PA41pRlHkVQeFqPFVDVfHTL0RHN";
    private static final String CURRENCY_ENDPOINT = "currencies";
    private static final String EXCHANGE_RATE_ENDPOINT = "latest";

    @Override
    public List<Currency> getCurrencies() {
        return formatCurrencyListFor(fetchContentsOf(getCurrencyEndpoint()));
    }

    @Override
    public Currency getCurrencyFor(String code) {
        try {
            return getCurrenciesFor(List.of(code)).stream().findFirst().orElse(Currency.NULL);
        } catch (NoSuchElementException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Currency> getCurrenciesFor(List<String> codes) {
        return formatCurrencyListFor(fetchContentsOf(getCurrencyEndpointFor(codes)));
    }

    private List<Currency> formatCurrencyListFor(String rawResponse) {
        Map<String, JsonElement> entries = getEntriesFromData(rawResponse);
        return entries.keySet().stream()
                .map(s -> (CurrencyAPIResponse) pojoFrom(entries.get(s), CurrencyAPIResponse.class))
                .map(CurrencyAdapter::adapt)
                .toList();
    }

    @Override
    public List<ExchangeRate> getExchangeRates() {
        return formatExchangeRatesWithDefaultBaseFor(fetchContentsOf(getExchangeRatesEndpoint()));
    }

    private List<ExchangeRate> formatExchangeRatesWithDefaultBaseFor(String rawResponse) {
        Currency defaultCurrency = new Currency("$", "US Dollar", "USD");
        return formatExchangeRatesFor(rawResponse, defaultCurrency);
    }

    @Override
    public List<ExchangeRate> getExchangeRatesFor(String baseCode) {
        Currency base = getCurrencyFor(baseCode);
        return formatExchangeRatesFor(fetchContentsOf(getExchangeRatesEndpoint()), base);
    }

    @Override
    public List<ExchangeRate> getExchangeRatesFor(String baseCode, List<String> codes) {
        Currency base = getCurrencyFor(baseCode);
        return formatExchangeRatesFor(fetchContentsOf(getExchangeRatesEndpointWithBaseFor(codes, baseCode)), base);
    }

    private List<ExchangeRate> formatExchangeRatesFor(String rawResponse, Currency base) {
        Map<String, JsonElement> entries = getEntriesFromData(rawResponse);
        List<ExchangeRateAPIResponse> pojos = entries.keySet().stream()
                .map(er -> exchangeRatePojoFrom(er, entries.get(er)))
                .toList();

        List<Currency> currencies = getCurrenciesFor(entries.keySet().stream().toList());
        ExchangeRateAdapter adapter = new ExchangeRateAdapter(currencies, base);

        return pojos.stream().map(adapter::adapt).toList();
    }

    private ExchangeRateAPIResponse exchangeRatePojoFrom(String code, JsonElement jsonElement) {
        return new ExchangeRateAPIResponse(code, jsonElement.getAsDouble());
    }

    private Object pojoFrom(JsonElement jsonElement, Class pojoClass) {
        return new Gson().fromJson(jsonElement, pojoClass);
    }

    private Map<String, JsonElement> getEntriesFromData(String rawResponse) {
        return new Gson().fromJson(rawResponse, JsonObject.class).get("data").getAsJsonObject().asMap();
    }

    private URL getExchangeRatesEndpoint() {
        try {
            return new URL(insertApiKey(createEndpoint(EXCHANGE_RATE_ENDPOINT)));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private URL getExchangeRatesEndpointWithBaseFor(List<String> codes, String baseCode) {
        String baseURL = insertApiKey(createEndpoint(EXCHANGE_RATE_ENDPOINT));
        try {
            return new URL(
                    baseURL +
                            "&base_currency=" + baseCode +
                            "&currencies=" + String.join(",", codes));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private String fetchContentsOf(URL url) {
        try {
            Connection.Response response = Jsoup.connect(
                    url.toString()
            ).ignoreContentType(true).method(Connection.Method.GET).execute();

            return response.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private URL getCurrencyEndpointFor(List<String> codes) {
        String baseURL = insertApiKey(createEndpoint(CURRENCY_ENDPOINT));
        try {
            return new URL(baseURL + "&currencies=" + String.join(",", codes));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private URL getCurrencyEndpoint() {
        try {
            return new URL(insertApiKey(createEndpoint(CURRENCY_ENDPOINT)));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private String insertApiKey(String url) {
        return url + "?apikey=" + API_KEY;
    }

    private String createEndpoint(String endpoint) {
        return API_ENDPOINT + endpoint;
    }
}
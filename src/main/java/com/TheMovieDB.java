package com;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TheMovieDB {
    private static final SQLiteReusableMethods DATABASE = new SQLiteReusableMethods();
    private static final DateTimeFormatter FORMATDATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static Connection.Response response;


    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        DATABASE.displayInitialisedCinemas();

        saveInDatabaseByDate();

    }

    private static void saveInDatabaseByDate() throws IOException {
        LocalDate firstDate = LocalDate.of(1900, 1, 1);
        saveInDatabase(FORMATDATE.format(firstDate), FORMATDATE.format(firstDate.plusYears(90)));
        firstDate = firstDate.plusYears(90);
        saveInDatabase(FORMATDATE.format(firstDate), FORMATDATE.format(firstDate.plusYears(25)));
        firstDate = firstDate.plusYears(25);
        saveInDatabase(FORMATDATE.format(firstDate), FORMATDATE.format(firstDate.plusYears(5)));
        firstDate = firstDate.plusYears(5);
        saveInDatabase(FORMATDATE.format(firstDate), FORMATDATE.format(firstDate.plusYears(1)));
        firstDate = firstDate.plusYears(1);
        saveInDatabase(FORMATDATE.format(firstDate), FORMATDATE.format(firstDate.plusYears(1)));
        firstDate = firstDate.plusYears(1);
        saveInDatabase(FORMATDATE.format(firstDate), FORMATDATE.format(firstDate.plusYears(1)));
        firstDate = firstDate.plusYears(1);
        saveInDatabase(FORMATDATE.format(firstDate), FORMATDATE.format(firstDate.plusYears(1)));

    }

    private static void saveInDatabase(String firstDate, String lastDate) throws IOException {

        String url = "https://www.themoviedb.org/discover/movie/items";

        boolean flag = true;

        for (int i = 1; i < 500&flag; i++) {
            String dynamicRequestBody = "air_date.gte=&air_date.lte=2022-11-11&certification=&certification_country=CH" +
                    "&debug=&first_air_date.gte=&first_air_date.lte=&ott_region=CH&page="+i+"&primary_release_date.gte=" +
                    "&primary_release_date.lte=&region=&release_date.gte="+firstDate+"&release_date.lte=" +lastDate+
                    "&show_me=0&sort_by=popularity.desc&vote_average.gte=0&vote_average.lte=100&vote_count.gte=100" +
                    "&with_genres=&with_keywords=&with_networks=&with_origin_country=&with_original_language=" +
                    "&with_ott_monetization_types=&with_ott_providers=&with_release_type=&with_runtime.gte=0" +
                    "&with_runtime.lte=40000";

            try {
                response = Jsoup.connect(url).timeout(15000)
                        .ignoreContentType(true)
                        .requestBody(dynamicRequestBody)
                        .method(Connection.Method.POST)
                        .execute();
            }catch (SocketTimeoutException e) {
                e.printStackTrace();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            Document document = response.parse();
            System.out.println(document.select("div.card.style_1 div").outerHtml().isEmpty()+"kacinci i dongusu"+i);
            if (document.select("div.card.style_1 div").outerHtml().isEmpty()) {
                flag=false;
            }
            for (Element each : document.select("div.card.style_1")) {
                if (!each.select(".poster").isEmpty()) {
                    String image = each.select(".poster").attr("abs:src");
                    String text = each.select("h2>a").text();
                    DATABASE.addCinema(text, image);
                    System.out.println("from " + firstDate + " to " + lastDate + " " + i + ". page text and image = " + text + " " + image);
                }
            }

        }

    }
}


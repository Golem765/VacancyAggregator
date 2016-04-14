package com.javarush.test.level28.lesson15.big01.model;

import com.javarush.test.level28.lesson15.big01.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Golem765 on 13.04.2016.
 */
public class HHStrategy implements Strategy
{
    private static final String URL_FORMAT = "http://hh.ua/search/vacancy?text=java+%s&page=%d";

    @Override
    public List<Vacancy> getVacancies(String searchString)
    {
        List<Vacancy> list = new ArrayList<>();
        for (int i = 0; ; i++)
        {
            try
            {
                Document doc = getDocument(searchString, i);
                if(doc == null)
                    break;
                Elements vaks = doc.select("[data-qa=vacancy-serp__vacancy]");
                if(vaks.isEmpty())
                    break;
                if (!vaks.isEmpty())
                {
                    for (Element e : vaks)
                    {
                        Vacancy vacancy = new Vacancy();
                        vacancy.setCity(e.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-address").text());
                        vacancy.setUrl(e.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").attr("href"));
                        vacancy.setTitle(e.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").text());
                        vacancy.setCompanyName(e.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer").text());
                        vacancy.setSiteName(doc.title());
                        if (!e.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation").isEmpty())
                            vacancy.setSalary(e.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation").text());
                        else
                            vacancy.setSalary("");
                        list.add(vacancy);
                    }
                }
            }
            catch (IOException e){}
        }
        return list;
    }
    protected Document getDocument(String searchString, int page) throws IOException
    {
        Document doc;
        String s = String.format(URL_FORMAT, searchString, page);
        doc = Jsoup.connect(s).userAgent("Chrome/49.0.2623.112").referrer("http://www.google.com").get();
        return doc;
    }
}

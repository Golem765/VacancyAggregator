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
 * Created by Golem765 on 15.04.2016.
 */
public class MoikrugStrategy implements Strategy
{
    private static final String URL_FORMAT = "https://moikrug.ru/vacancies?q=java+%s&page=%d";

    @Override
    public List<Vacancy> getVacancies(String searchString)
    {
        List<Vacancy> list = new ArrayList<>();
        for (int i = 1; ; i++)
        {
            try
            {
                Document doc = getDocument(searchString, i);
                if(doc == null)
                    break;
                Elements vaks = doc.select(".job ");
                vaks.addAll(doc.select(".job marked"));
                if(vaks.isEmpty())
                    break;
                if (!vaks.isEmpty())
                {
                    for (Element e : vaks)
                    {
                        Vacancy vacancy = new Vacancy();
                        vacancy.setSiteName(doc.title());
                        vacancy.setSalary(e.getElementsByClass("salary").first().getElementsByAttributeValue("title", "Зарплата").text());
                        vacancy.setTitle(e.getElementsByClass("info").first().getElementsByAttribute("title").text());
                        vacancy.setUrl("https://moikrug.ru" + e.getElementsByClass("title").first().getElementsByTag("a").attr("href"));
                        vacancy.setCity(e.getElementsByClass("location").text());
                        vacancy.setCompanyName(e.getElementsByClass("company_name").first().getElementsByTag("a").text());
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
        doc = Jsoup.connect(s).userAgent("Chrome/49.0.2623.112").get();
        return doc;
    }
}

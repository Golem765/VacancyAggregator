package com.javarush.test.level28.lesson15.big01.view;

import com.javarush.test.level28.lesson15.big01.Controller;
import com.javarush.test.level28.lesson15.big01.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Golem765 on 14.04.2016.
 */
public class HtmlView implements View
{
    private Controller controller;
    private final String filePath = "./src/"+this.getClass().getPackage().getName().replace(".","/")+"/vacancies.html";
    @Override
    public void update(List<Vacancy> vacancies)
    {
        try
        {
            String s = getUpdatedFileContent(vacancies);
            updateFile(s);
        }
        catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void setController(Controller controller)
    {
        this.controller = controller;
    }

    public void userCitySelectEmulationMethod()
    {
        controller.onCitySelect("Dnepropetrovsk");
    }

    private String getUpdatedFileContent(List<Vacancy> vacancies)
    {
        String ret;
        try
        {
            Document doc = getDocument();
            Element base = doc.select(".template").first();
            Element element = base.clone();
            element.removeClass("template");
            element.removeAttr("style");
            doc.select("tr[class=vacancy]").remove();
            for (Vacancy v : vacancies)
            {
                Element e = element.clone();
                e.select(".city").first().text(v.getCity());
                e.select(".companyName").first().text(v.getCompanyName());
                e.select(".salary").first().text(v.getSalary());
                Element ref = e.select("a").first();
                ref.text(v.getTitle());
                ref.attr("href", v.getUrl());
                base.before(e.outerHtml());
            }
            ret = doc.html();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            ret = "Some exception occurred";
        }
        return ret;
    }
    protected Document getDocument() throws IOException
    {
        File file = new File(filePath);
        Document doc = Jsoup.parse(file, "UTF-8");
        return doc;
    }
    private void updateFile(String string)throws IOException
    {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath, false)))
        {
            bufferedWriter.write(string);
        }
    }
}

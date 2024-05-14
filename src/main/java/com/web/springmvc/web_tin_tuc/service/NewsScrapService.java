package com.web.springmvc.web_tin_tuc.service;

import com.web.springmvc.web_tin_tuc.dto.NewsDTO;
import com.web.springmvc.web_tin_tuc.repository.CategoryRepository;
import com.web.springmvc.web_tin_tuc.repository.NewsRepository;
import com.web.springmvc.web_tin_tuc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsScrapService {
    private String url = "https://dantri.com.vn/";
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    public NewsDTO getFirstNews() {
        try {
            NewsDTO newsDTO = new NewsDTO();
            Document document = Jsoup.connect(url).get();
            Element element = document.getElementsByClass("article highlight").first();
            String urlDetail = "https://dantri.com.vn"+element.child(0).child(0).child(0).attr("href");
            System.out.println(urlDetail);
            Document document1 = Jsoup.connect(urlDetail).get();
            Element element1 = document1.getElementsByClass("e-magazine bg-wrap d-magazine").first();
            if(element1 == null) return null;
//
//            //Xu ly title, description, role
            newsDTO.setTitle(element1.getElementsByClass("e-magazine__title").text());
            newsDTO.setUser(userRepository.findByRole());
            newsDTO.setShortDescription(element1.getElementsByClass("e-magazine__sapo").text());
//
//            // Xu ly content
            Element element2 = document1.getElementsByClass("e-magazine__body dnews__body").first();
            String content = "";
            Elements elements = element2.children();
            for(Element element3: elements) {
                if(element3.tagName().equals("p")){
                    content += element3.outerHtml();
                }
            }
            newsDTO.setContent(content);
//
//            //Xu ly category
            String categoryName = document1.getElementsByClass("category-name").first().child(0).text();
            if(categoryName == null) return null;
            newsDTO.setCategory(categoryRepository.findByName(categoryName).getId());
//
//            //Xu ly thumbnail
            newsDTO.setThumbnail(element.getElementsByClass("article-thumb").first().child(0).child(0).attr("src"));
            return newsDTO;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

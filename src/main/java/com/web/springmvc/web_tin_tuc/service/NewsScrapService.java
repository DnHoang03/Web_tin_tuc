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
    private String url = "https://vnexpress.net/the-gioi";
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    public NewsDTO getFirstNews() {
        try {
            NewsDTO newsDTO = new NewsDTO();
            Document document = Jsoup.connect(url).get();
            Element element = document.getElementsByClass("item-news full-thumb article-topstory").first();
            assert element != null;
            String urlDetail = element.child(1).child(0).attr("href");
            System.out.println(urlDetail);
            Document document1 = Jsoup.connect(urlDetail).get();
            Element element1 = document1.getElementsByClass("sidebar-1").first();
//
//            //Xu ly title, description, role
            assert element1 != null;
            newsDTO.setTitle(element1.getElementsByClass("title-detail").text());
            newsDTO.setUser(userRepository.findByRole());
            newsDTO.setShortDescription(element1.getElementsByClass("description").text());
            newsDTO.setThumbnail(element.child(0).child(0).child(0).child(1).attr("src"));
//
//            // Xu ly content
            Element element2 = document1.getElementsByClass("fck_detail").first();
            String content = "";
            assert element2 != null;
            Elements elements = element2.children();
            for(Element element3: elements) {
                if(element3.tagName().equals("p")){
                    content += element3.outerHtml();
                }
            }
            newsDTO.setContent(content);
//
//            //Xu ly category
            String categoryName = document1.getElementsByClass("header-content width_common").first().child(0).child(0).child(0).attr("title");
            if(categoryName == null) return null;
            newsDTO.setCategory(categoryRepository.findByName(categoryName).getId());
//
//            //Xu ly thumbnail
            newsDTO.setThumbnail(element.getElementsByClass("thumb-art").first().child(0).child(0).child(1).attr("src"));
            return newsDTO;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

package com.example.helldefender.rvfunction.data.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Helldefender on 2016/11/29.
 */

public class NewsBean {
    /**
     * date : 20140523
     * stories : [{"title":"中国古代家具发展到今天有两个高峰，一个两宋一个明末（多图）","ga_prefix":"052321","images":["http://p1.zhimg.com/45/b9/45b9f057fc1957ed2c946814342c0f02.jpg"],"type":0,"id":3930445},"..."]
     * top_stories : [{"title":"商场和很多人家里，竹制家具越来越多（多图）","image":"http://p2.zhimg.com/9a/15/9a1570bb9e5fa53ae9fb9269a56ee019.jpg","ga_prefix":"052315","type":0,"id":3930883},"..."]
     */

    private String date;
    /**
     * title : 中国古代家具发展到今天有两个高峰，一个两宋一个明末（多图）
     * ga_prefix : 052321
     * images : ["http://p1.zhimg.com/45/b9/45b9f057fc1957ed2c946814342c0f02.jpg"]
     * type : 0
     * id : 3930445
     */

    private List<StoriesBean> stories;
    /**
     * title : 商场和很多人家里，竹制家具越来越多（多图）
     * image : http://p2.zhimg.com/9a/15/9a1570bb9e5fa53ae9fb9269a56ee019.jpg
     * ga_prefix : 052315
     * type : 0
     * id : 3930883
     */

    private List<TopStoriesBean> top_stories;
    /**
     * description : 为你发现最有趣的新鲜事，建议在 WiFi 下查看
     * background : http://pic1.zhimg.com/a5128188ed788005ad50840a42079c41.jpg
     * color : 8307764
     * name : 不许无聊
     * image : http://pic3.zhimg.com/da1fcaf6a02d1223d130d5b106e828b9.jpg
     * editors : [{"url":"http://www.zhihu.com/people/wezeit","bio":"微在 Wezeit 主编","id":70,"avatar":"http://pic4.zhimg.com/068311926_m.jpg","name":"益康糯米"},"..."]
     * image_source :
     */

    private String description;
    private String background;
    private int color;
    private String name;
    private String image;
    private String image_source;
    /**
     * url : http://www.zhihu.com/people/wezeit
     * bio : 微在 Wezeit 主编
     * id : 70
     * avatar : http://pic4.zhimg.com/068311926_m.jpg
     * name : 益康糯米
     */

    private List<EditorsBean> editors;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public List<TopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStoriesBean> top_stories) {
        this.top_stories = top_stories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public List<EditorsBean> getEditors() {
        return editors;
    }

    public void setEditors(List<EditorsBean> editors) {
        this.editors = editors;
    }

    public static class StoriesBean {
        private String title;
        private String ga_prefix;
        private int type;
        private int id;
        private List<String> images;
        private String data;
        private String weekend;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getWeekend() {
            handleWeekend();
            return weekend;
        }

        public void setWeekend(String weekend) {
            this.weekend = weekend;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        private void handleWeekend() {
            Calendar calendar = Calendar.getInstance();
            Date today = calendar.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            String current = simpleDateFormat.format(today);
            if (data.equals(current)) {
                weekend = "今日新闻";
            } else {
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyMMdd");
                try {
                    Calendar mCanlender = Calendar.getInstance();
                    mCanlender.setTime(simpleDateFormat1.parse(data));
                    String week="";
                    switch (mCanlender.get(Calendar.DAY_OF_WEEK)){
                        case 1:
                            week = "星期四";
                            break;
                        case 2:
                            week = "星期五";
                            break;
                        case 3:
                            week = "星期六";
                            break;
                        case 4:
                            week = "星期日";
                            break;
                        case 5:
                            week = "星期一";
                            break;
                        case 6:
                            week = "星期二";
                            break;
                        case 7:
                            week = "星期三";
                            break;
                    }
                    weekend=data.substring(4,6)+"月"+data.substring(6)+"日"+"  "+week;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class TopStoriesBean {
        private String title;
        private String image;
        private String ga_prefix;
        private int type;
        private int id;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class EditorsBean {
        private String url;
        private String bio;
        private int id;
        private String avatar;
        private String name;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

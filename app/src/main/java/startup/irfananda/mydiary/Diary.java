package startup.irfananda.mydiary;

import java.io.Serializable;
import java.util.List;

public class Diary implements Serializable{
    private int id_story;
    private String story;
    private String conclusion;
    private String date;
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId_story() {
        return id_story;
    }

    public void setId_story(int id_story) {
        this.id_story = id_story;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

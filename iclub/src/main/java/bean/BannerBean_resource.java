package bean;

public class BannerBean_resource {
    private String id;
    private String title;
    private int resource;
    public BannerBean_resource(String id, String title, int resource) {
        this.id = id;
        this.title = title;
        this.resource = resource;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
}

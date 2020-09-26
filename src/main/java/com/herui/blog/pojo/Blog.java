package com.herui.blog.pojo;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@Entity
@Table(name = "t_blog")
public class Blog {

    @Id
    @GeneratedValue // 自动生成
    private Long id;
    private String title;

    @Basic(fetch = FetchType.LAZY) // 懒加载
    @Lob // 大文本
    private String content;
    private String firstPicture;
    private String flag;
    private Integer views;
    private boolean appreciation;
    private boolean shareStatement;
    private boolean commentabled;
    private boolean published;
    private boolean recommend;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creatTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @ManyToOne
    private Type type;
    @ManyToMany(cascade = {CascadeType.PERSIST}) // 级联新增
    private List<Tag> tags = new ArrayList<>();
    @Transient // 不保存到数据库,不进行数据库映射
    private String names;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "blog")
    private List<Comment> comments = new ArrayList<>();
    private String description;
    public void init(){
        this.names = tagsToIds(this.getTags());
    }

    private String tagsToIds(List<Tag> tags){
        if (!tags.isEmpty()){
            StringBuffer ids = new StringBuffer();
            boolean flag = false;
            for (Tag tag : tags){
                if (flag){
                    ids.append(",");
                }else{
                    flag=true;
                }
                ids.append(tag.getName());
            }
            return ids.toString();
        }else{
            return names;
        }
    }
}
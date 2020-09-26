package com.herui.blog.pojo;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "t_tag")
@Data
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "标签名不能为空")
    private String name;
    @ManyToMany(mappedBy = "tags") // 两个实体类建立关系
    private List<Blog> blogs = new ArrayList<>();
}
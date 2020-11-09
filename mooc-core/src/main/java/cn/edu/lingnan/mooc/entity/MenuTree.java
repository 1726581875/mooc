package cn.edu.lingnan.mooc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author xmz
 * @date: 2020/11/08
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuTree {

    @Id
    private Integer id;

    private String label;

    private Integer parentId;
    
    private String permission;

    private String router;

    private Integer leaf;

}

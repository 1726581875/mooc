package cn.edu.lingnan.mooc.portal.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/08
 */
@Data
public class MenuTreeDTO implements Serializable {

   private Integer id;

   private String label;

   private List<MenuTreeDTO> children;

}

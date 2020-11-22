package cn.edu.lingnan.mooc.model;

/**
 * @author xmz
 * @date: 2020/11/22
 */
public class Role {

    private Long id;

    private String name;

    public Role(){}

    public Role(long id,String name){
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

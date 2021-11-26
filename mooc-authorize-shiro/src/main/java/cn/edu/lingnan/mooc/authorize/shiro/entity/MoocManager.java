package cn.edu.lingnan.mooc.authorize.shiro.entity;

/**
 * @author xmz
 * @date: 2020/11/22
 */
public class MoocManager {

    private Long id;

    private String name;

    private String account;

    private String password;

    private Integer status;

    public MoocManager() {}

    public MoocManager(Long id, String name, String account, String password, Integer status) {
        this.id = id;
        this.name = name;
        this.account = account;
        this.password = password;
        this.status = status;
    }

    public static MoocManager build(){
        return new MoocManager();
    }

    public Long getId() {
        return id;
    }

    public MoocManager setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MoocManager setName(String name) {
        this.name = name;
        return this;
    }

    public String getAccount() {
        return account;
    }

    public MoocManager setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public MoocManager setPassword(String password) {
        this.password = password;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public MoocManager setStatus(Integer status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "MoocManager{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                '}';
    }
}

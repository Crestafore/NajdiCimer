package mk.mlabs.najdicimer.model;

/**
 * Created by Darko on 4/14/2016.
 */
public class BaseEntity implements IBaseEntity {
    private Long id;

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }
}

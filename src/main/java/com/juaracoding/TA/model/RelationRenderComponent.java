package com.juaracoding.TA.model;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "MstRelationRenderComponent")
public class RelationRenderComponent {

    @Id
    @Column(name = "RelationRenderComponentID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long relationRenderComponentId;
    @Column(name = "RelationRenderComponentName")
    private String relationRenderComponentName;
    @ManyToMany(mappedBy = "relationRenderComponentList")
    private List<RenderComponent> renderComponentList;
    public Long getRelationRenderComponentId() {
        return relationRenderComponentId;
    }
    public void setRelationRenderComponentId(Long relationRenderComponentId) {
        this.relationRenderComponentId = relationRenderComponentId;
    }
    public String getRelationRenderComponentName() {
        return relationRenderComponentName;
    }
    public void setRelationRenderComponentName(String relationRenderComponentName) {
        this.relationRenderComponentName = relationRenderComponentName;
    }
    public List<RenderComponent> getRenderComponentList() {
        return renderComponentList;
    }
    public void setRenderComponentList(List<RenderComponent> renderComponentList) {
        this.renderComponentList = renderComponentList;
    }
}

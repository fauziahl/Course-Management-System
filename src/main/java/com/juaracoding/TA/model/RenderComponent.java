package com.juaracoding.TA.model;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "MstRenderComponent")
public class RenderComponent {

    @Id
    @Column(name = "RenderComponentID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long renderComponentId;
    @Column(name = "RenderComponentName")
    private String renderComponentName;
    @ManyToMany
    @JoinTable(name = "MapRenderComponentRelations",
            joinColumns = {
                    @JoinColumn(name = "RenderComponentID",referencedColumnName = "RenderComponentID")}, inverseJoinColumns = {
            @JoinColumn (name = "RelationRenderComponentID",referencedColumnName = "RelationRenderComponentID")}, uniqueConstraints = @UniqueConstraint(columnNames = {
            "RenderComponentID", "RelationRenderComponentID" }))
    private List<RelationRenderComponent> relationRenderComponentList;
    public Long getRenderComponentId() {
        return renderComponentId;
    }
    public void setRenderComponentId(Long renderComponentId) {
        this.renderComponentId = renderComponentId;
    }
    public String getRenderComponentName() {
        return renderComponentName;
    }
    public void setRenderComponentName(String renderComponentName) {
        this.renderComponentName = renderComponentName;
    }
    public List<RelationRenderComponent> getRelationRenderComponentList() {
        return relationRenderComponentList;
    }
    public void setRelationRenderComponentList(List<RelationRenderComponent> relationRenderComponentList) {
        this.relationRenderComponentList = relationRenderComponentList;
    }
}
package com.juaracoding.TA.dto;

import java.util.List;

public class RenderComponentDTO {
    private Long renderComponentId;

    private String renderComponentName;

    private List<RelationRenderComponentDTO> relationRenderComponentList;

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

    public List<RelationRenderComponentDTO> getRelationRenderComponentList() {
        return relationRenderComponentList;
    }

    public void setRelationRenderComponentList(List<RelationRenderComponentDTO> relationRenderComponentList) {
        this.relationRenderComponentList = relationRenderComponentList;
    }
}

package com.juaracoding.TA.dto;

import java.util.List;

public class RelationRenderComponentDTO {
    private Long relationRenderComponentId;
    private String relationRenderComponentName;
    private List<RenderComponentDTO> renderComponentList;

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

    public List<RenderComponentDTO> getRenderComponentList() {
        return renderComponentList;
    }

    public void setRenderComponentList(List<RenderComponentDTO> renderComponentList) {
        this.renderComponentList = renderComponentList;
    }
}

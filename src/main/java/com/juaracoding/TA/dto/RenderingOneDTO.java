package com.juaracoding.TA.dto;

import java.util.List;

public class RenderingOneDTO {

    private Long renderingOneId;

    private String renderingOneName;

    private List<RenderingTwoDTO> listRenderingTwo;

    public Long getRenderingOneId() {
        return renderingOneId;
    }

    public void setRenderingOneId(Long renderingOneId) {
        this.renderingOneId = renderingOneId;
    }

    public String getRenderingOneName() {
        return renderingOneName;
    }

    public void setRenderingOneName(String renderingOneName) {
        this.renderingOneName = renderingOneName;
    }

    public List<RenderingTwoDTO> getListRenderingTwo() {
        return listRenderingTwo;
    }

    public void setListRenderingTwo(List<RenderingTwoDTO> listRenderingTwo) {
        this.listRenderingTwo = listRenderingTwo;
    }
}

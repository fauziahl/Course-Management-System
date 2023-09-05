package com.juaracoding.TA.dto;

import com.juaracoding.TA.utils.ConstantMessage;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class MenuDTO {

    private Long idMenu;


    /*
       validasi field namaMenu not null not empty
       length max 25
    */
    @NotNull
    @NotEmpty
    @Length(message = ConstantMessage.WARNING_MENU_NAME_LENGTH,max = 25)
    private String namaMenu;


    /*
       validasi field pathMenu not null not empty
       length max 50
    */
    @NotNull
    @NotEmpty
    @Length(message = ConstantMessage.WARNING_MENU_NAME_LENGTH,max = 50)
    private String pathMenu;

    /*
      validasi field endPoint not null not empty
      length max 50
   */
    @NotNull
    @NotEmpty
    @Length(message = ConstantMessage.WARNING_MENU_NAME_LENGTH,max = 50)
    private String endPoint;


//    @JsonIgnoreProperties("listMenuAkses")
    private List<AksesDTO> listAksesMenu;

    private MenuHeaderDTO menuHeader;

    public Long getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Long idMenu) {
        this.idMenu = idMenu;
    }

    public MenuHeaderDTO getMenuHeader() {
        return menuHeader;
    }

    public void setMenuHeader(MenuHeaderDTO menuHeader) {
        this.menuHeader = menuHeader;
    }

    public List<AksesDTO> getListAksesMenu() {
        return listAksesMenu;
    }

    public void setListAksesMenu(List<AksesDTO> listAksesMenu) {
        this.listAksesMenu = listAksesMenu;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public String getPathMenu() {
        return pathMenu;
    }

    public void setPathMenu(String pathMenu) {
        this.pathMenu = pathMenu;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }
}
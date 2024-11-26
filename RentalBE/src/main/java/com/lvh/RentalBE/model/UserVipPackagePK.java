/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lvh.RentalBE.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

/**
 *
 * @author levan
 */
@Embeddable
public class UserVipPackagePK implements Serializable {

    @Basic(optional = false)
    @Column(name = "user_id")
    private long userId;
    @Basic(optional = false)
    @Column(name = "vip_package_id")
    private long vipPackageId;

    public UserVipPackagePK() {
    }

    public UserVipPackagePK(long userId, long vipPackageId) {
        this.userId = userId;
        this.vipPackageId = vipPackageId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getVipPackageId() {
        return vipPackageId;
    }

    public void setVipPackageId(long vipPackageId) {
        this.vipPackageId = vipPackageId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) userId;
        hash += (int) vipPackageId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserVipPackagePK)) {
            return false;
        }
        UserVipPackagePK other = (UserVipPackagePK) object;
        if (this.userId != other.userId) {
            return false;
        }
        if (this.vipPackageId != other.vipPackageId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.lvh.RentalBE.models.UserVipPackagePK[ userId=" + userId + ", vipPackageId=" + vipPackageId + " ]";
    }

}

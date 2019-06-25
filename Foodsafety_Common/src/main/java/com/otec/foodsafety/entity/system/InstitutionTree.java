package com.otec.foodsafety.entity.system;

import com.otec.foodsafety.util.Tree;

public class InstitutionTree extends Tree<InstitutionTree> {

    public InstitutionTree() {
    }

    public InstitutionTree(SysInstitution r) {
        this.setText(r.getInstitutionName());
        this.setId(String.valueOf(r.getInstitutionId()));
        this.setExpanded(expanded);
        this.setPid(String.valueOf(r.getpInstitutionId()));
        this.setValue(String.valueOf(r.getInstitutionId()));
        this.setLabel(r.getInstitutionName());
        this.setType(Integer.valueOf(r.getInstitutionType()));
    }
}

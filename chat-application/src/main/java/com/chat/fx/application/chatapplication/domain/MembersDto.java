package com.chat.fx.application.chatapplication.domain;

import java.util.List;

public class MembersDto {
    private int total;
    private List<MemberDto> members;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<MemberDto> getMembers() {
        return members;
    }

    public void setMembers(List<MemberDto> members) {
        this.members = members;
    }
}

package org.lpw.ranch.group.member;

import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(MemberModel.NAME + ".service")
public class MemberServiceImpl implements MemberService {
    @Inject
    private MemberDao memberDao;
}

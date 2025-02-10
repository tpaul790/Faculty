package com.example.socialnetworkgui.repository.database;

import com.example.socialnetworkgui.domain.*;
import com.example.socialnetworkgui.dto.FiltruDto;
import com.example.socialnetworkgui.utils.paging.PagingRepository;

public interface FriendshipRepository extends PagingRepository<Tuple<Integer,Integer>, Friendship> {
    Page<User> findAllFriendsOnPage(int userId, Pageable pageable, FiltruDto filtru);
}

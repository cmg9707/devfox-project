package com.example.demo1dww.mapper;

import com.example.demo1dww.domain.TeamDTO;
import com.example.demo1dww.domain.TeamchatDTO;
import com.example.demo1dww.domain.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface DMapper {

    public int ck();

    public int User_Chk(@Param("username") String username , @Param("user_value") String user_value);

    public void User_Insert(UserDTO dto);

    public int Login_CK(UserDTO dto);

    public int Email_Ck(@Param("email") String email);

    public void Passsw(@Param("user_pass") String user_pass , @Param("email") String email);

    public UserDTO User_Select(@Param("user_id") String user_id);

    public UserDTO Mail_User(@Param("email") String email);

    public void Team_Insert(@Param("teamname") String teamname , @Param("myemail") String myemail);

    public UserDTO Leader_Select(@Param("email") String email);

    public void Member_Insert(UserDTO dto);

    public int Seq_Select();

    public void Member_Insert_no(@Param("teamno") int teamno , @Param("user_id") String user_id,
                                 @Param("nikname") String nikname);

    public List<TeamDTO> Team_Select (@Param("user_id") String user_id);

    public List<TeamDTO> Member_List (@Param("teamno") int teamno);

    public List<TeamDTO> Work_Select (@Param("teamno") int teamno);

    public void Work_Insert (TeamDTO dto);

    public List<TeamchatDTO> TeamChat_Select(@Param("teamno") int teamno);

    public List<TeamDTO> Teamtoday_Work(@Param("teamno") int teamno, @Param("start_date") String start_date);

    public TeamDTO My_team(@Param("teamno") int teamno);

    public void TeamChat_Insert(TeamchatDTO dto);

    public void COMPLETE_Up(TeamDTO dto);

    public void Work_Delect(TeamDTO dto);

    public void User_Up(Map<String, Object> params);

    public void Team_del(TeamDTO dto);
}

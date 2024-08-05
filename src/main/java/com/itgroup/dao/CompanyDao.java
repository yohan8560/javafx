package com.itgroup.dao;

import com.itgroup.bean.Company;
import com.itgroup.utility.Paging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDao extends SuperDao{
    public CompanyDao() {
    }

    public List<Company> selectAll() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = " select * from companys order by name desc ";
        ResultSet rs = null;

        List<Company> allData = new ArrayList<Company>();

        try{
            conn = super.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Company bean = this.makeBean(rs);
                allData.add(bean);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            try{
                if (rs != null){rs.close();}
                if (pstmt != null){pstmt.close();}
                if (conn != null){conn.close();}
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        return allData;
    }


    private Company makeBean(ResultSet rs) {
        Company bean = new Company();
        try {
            bean.setPnum(rs.getInt("pnum"));
            bean.setName(rs.getString("name"));
            bean.setCeoName(rs.getString("ceoName"));
            bean.setImage(rs.getString("image"));
            bean.setComSize(rs.getString("comSize"));
            bean.setTalent(rs.getString("talent"));
            bean.setOpening(rs.getString("opening"));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bean;
    }

    public List<Company> getPaginationData(Paging pageInfo) {
        Connection conn = null;

        String sql = " select pnum, name, ceoName, image, comSize, talent, opening ";
        sql += " from ( ";
        sql += " select pnum, name, ceoName, image, comSize, talent, opening, ";
        sql += " rank() over (order by pnum desc) as ranking ";
        sql += " from companys ";

        // mode가 'all'이 아니면 where 절이 추가로 필요합니다.
        String mode = pageInfo.getMode();
        System.out.println(mode);
        boolean bool = mode.equals(null) || mode.equals("null") || mode.equals("") || mode.equals("all");

        if (!bool) {
            sql += " where comSize = ? ";
        }


        sql += " ) ";
        sql += " where ranking between ? and ? ";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Company> allData = new ArrayList<>();
        try {
            conn = super.getConnection();
            pstmt = conn.prepareStatement(sql);

            if (!bool) {
                pstmt.setString(1, mode);
                pstmt.setInt(2, pageInfo.getBeginRow());
                pstmt.setInt(3, pageInfo.getEndRow());
            } else {
                pstmt.setInt(1, pageInfo.getBeginRow());
                pstmt.setInt(2, pageInfo.getEndRow());
            }


            rs = pstmt.executeQuery();

            while (rs.next()) {

                Company bean = this.makeBean(rs);
                allData.add(bean);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return allData;
        }
    }

    public int getTotalCount(String comSize) {
        int totalCount = 0;

        String sql = " select count(*) as mycnt from companys ";
        boolean bool = comSize == null || comSize.equals("all");
        if (!bool) {
            sql += " where comSize = ? ";
        }

        Connection conn = null;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = super.getConnection();
            pstmt = conn.prepareStatement(sql);

            if (!bool) {
                pstmt.setString(1, comSize);
            }

            rs = pstmt.executeQuery();

            if (rs.next()) {
                totalCount = rs.getInt("mycnt");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return totalCount;
    }

    public int insertData(Company bean) {
        System.out.println(bean);
        int cnt = -1;
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = " insert into companys(pnum, name, ceoName, image, comSize, talent, opening)";
        sql += " values(seqCompanys.NEXTVAL, ?, ?, ?, ?, ?, ?)";

        try {
            conn = super.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, bean.getName());
            pstmt.setString(2, bean.getCeoName());
            pstmt.setString(3, bean.getImage());
            pstmt.setString(4, bean.getComSize());
            pstmt.setString(5, bean.getTalent());
            pstmt.setString(6, bean.getOpening());

            cnt = pstmt.executeUpdate();

            conn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return cnt;

    }

    public int updateData(Company bean) {
        System.out.println(bean);
        int cnt = -1;
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "UPDATE companys SET name = ?, ceoName = ?, image = ?, comSize = ?, talent = ?, opening = ? WHERE pnum = ?";

        try {
            conn = super.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);

            // Set the parameters for the prepared statement
            pstmt.setString(1, bean.getName());
            pstmt.setString(2, bean.getCeoName());
            pstmt.setString(3, bean.getImage());
            pstmt.setString(4, bean.getComSize());
            pstmt.setString(5, bean.getTalent());
            pstmt.setString(6, bean.getOpening());
            pstmt.setInt(7, bean.getPnum());

            // Execute the update
            cnt = pstmt.executeUpdate();

            // Commit the transaction
            conn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return cnt;
    }


//    public int updateData(Company bean) {
//        System.out.println(bean);
//        int cnt = -1;
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//
//        String sql = " update companys set name = ? , ceoName = ? , image = ?, comSize = ?, talent = ? ,opening = ? ";
//        sql += " where pnum = ? ";
//
//        try {
//            conn = super.getConnection();
//            conn.setAutoCommit(false);
//            pstmt = conn.prepareStatement(sql);
//
//            pstmt.setString(1, bean.getName());
//            pstmt.setString(2, bean.getCeoName());
//            pstmt.setString(3, bean.getImage());
//            pstmt.setString(4, bean.getComSize());
//            pstmt.setString(5, bean.getTalent());
//            pstmt.setString(6, bean.getOpening());
//            pstmt.setString(7, bean.getPnum());
//
//            cnt = pstmt.executeUpdate();
//
//            conn.commit();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            try {
//                conn.rollback();
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//        } finally {
//            try {
//                if (pstmt != null) {
//                    pstmt.close();
//                }
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//
//        return cnt;
//
//    }

    public String getComSizeName(String comSize) {
        String comSizeName = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = " select * from companys where comsize = ? ";
        ResultSet rs = null;


        try{
            conn = super.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, comSize);
            rs = pstmt.executeQuery();


            if (rs.next()) {
                comSizeName = rs.getString("COMSIZE");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return comSizeName;
    }

    public int deleteData(int pnum) {
        System.out.println("기본 키 = " + pnum);
        int cnt = -1;
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = " delete from companys ";
        sql += " where pnum = ? ";

        try {
            conn = super.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);


            pstmt.setInt(1, pnum);

            cnt = pstmt.executeUpdate();

            conn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return cnt;
    }
}

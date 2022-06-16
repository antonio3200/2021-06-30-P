package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Arco;
import it.polito.tdp.genes.model.Genes;
import it.polito.tdp.genes.model.Interactions;


public class GenesDao {
	
	public List<Genes> getAllGenes(){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				result.add(genes);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
public List<String> getLocalization(){
	String sql="SELECT DISTINCT localization "
			+ "FROM classification";
	List<String> result= new LinkedList<String>();
	Connection conn= DBConnect.getConnection();
	try {
		PreparedStatement st= conn.prepareStatement(sql);
		ResultSet rs= st.executeQuery();
		while(rs.next()) {
			String s = rs.getString("localization");
			result.add(s);
		}
		conn.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw new RuntimeException("SQL ERROR");
	}
	return result;
}

public List<Arco> getArco(){
	String sql="select l1,l2, COUNT(DISTINCT  TYPE) AS sum "
			+ "from (SELECT c1.Localization AS l1 ,c2.Localization AS l2,i.GeneID1 AS id1,i.GeneID2 AS id2, i.`Type`"
			+ "		FROM  interactions i, classification c1, classification c2 "
			+ "		WHERE  i.GeneID1= c1.GeneID "
			+ "		AND  i.GeneID2=c2.GeneID "
			+ "		AND  c1.Localization<>c2.Localization "
			+ "		GROUP BY  c1.Localization ,c2.Localization,i.GeneID1,i.GeneID2 "
			+ "		ORDER  BY  c1.Localization,c2.Localization,i.GeneID1,i.GeneID2) a "
			+ "group by l1,l2 "
			+ "order by l1,l2 ";
		List<Arco> result= new LinkedList<Arco>();
		Connection conn= DBConnect.getConnection();
		try {
			PreparedStatement st= conn.prepareStatement(sql);
			ResultSet rs= st.executeQuery();
			while(rs.next()) {
				Arco a = new Arco(rs.getString("l1"),rs.getString("l2"),rs.getInt("sum"));
				result.add(a);
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("SQL ERROR");
		}
		return result;
}

	
}

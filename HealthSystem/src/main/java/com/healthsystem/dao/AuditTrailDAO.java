package com.healthsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.healthsystem.database.DatabaseConnectionFactory;
import com.healthsystem.model.AuditTrailModel;
import com.healthsystem.model.ErrorLogModel;

public class AuditTrailDAO {
	private Connection connection;

	public AuditTrailDAO() {
		this.connection = DatabaseConnectionFactory.getConnection();
	}

	public boolean add(AuditTrailModel auditTrailModel) {

		if (auditTrailModel.getIdUser() == null || auditTrailModel.getIdUser().toString().isEmpty()) {
			String sql = "INSERT INTO AUDIT_TRAIL " + "(id_audit_trail, category, event_name, additional_info) "
					+ " VALUES (?,?,?,?)";

			try {
				PreparedStatement stm = connection.prepareStatement(sql);

				stm.setString(1, auditTrailModel.getIdAuditTrail());
				stm.setString(2, auditTrailModel.getCategory());
				stm.setString(3, auditTrailModel.getEventName());
				stm.setString(4, auditTrailModel.getAdditionalInfo());

				stm.execute();
				stm.close();
				return true;

			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;
		} else {
			String sql = "INSERT INTO AUDIT_TRAIL "
					+ "(id_audit_trail, category, event_name, additional_info, id_user) " + " VALUES (?,?,?,?,?)";

			System.out.println( "aqqqq + " +  auditTrailModel.getIdUser());
			try {
				PreparedStatement stm = connection.prepareStatement(sql);

				stm.setString(1, auditTrailModel.getIdAuditTrail());
				stm.setString(2, auditTrailModel.getCategory());
				stm.setString(3, auditTrailModel.getEventName());
				stm.setString(4, auditTrailModel.getAdditionalInfo());
				stm.setString(5, auditTrailModel.getIdUser());

				stm.execute();
				stm.close();
				return true;

			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;

		}

	}

	public List<AuditTrailModel> list(String startDate) {
 
		List<AuditTrailModel> listAuditTrail = new ArrayList<>();
		String sql = " " +
" SELECT A.additional_info, A.category, CONVERT(datetime, SWITCHOFFSET(A.event_date, DATEPART(TZOFFSET,  " +
" A.event_date AT TIME ZONE 'E. South America Standard Time'))) AS event_date, A.event_name, A.id_audit_trail, A.id_user " +
" , U.login, U.name, U.photo " +
" FROM AUDIT_TRAIL AS A LEFT JOIN USER_HEALTH AS U  " +
" ON U.id_user = A.id_user WHERE CONVERT(DATE,  CONVERT(datetime, SWITCHOFFSET(A.event_date, DATEPART(TZOFFSET, " +
" A.event_date AT TIME ZONE 'E. South America Standard Time'))))= ? ORDER BY event_date DESC;";

		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			stm.setString(1, startDate);
			
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				AuditTrailModel auditTrailModel = new AuditTrailModel();
				
				auditTrailModel.setIdAuditTrail(rs.getString("id_audit_trail"));
				auditTrailModel.setEventDate(rs.getString("event_date"));
				auditTrailModel.setCategory(rs.getString("category"));
				auditTrailModel.setEventName(rs.getString("event_name"));
				auditTrailModel.setAdditionalInfo(rs.getString("additional_info"));
				auditTrailModel.setIdUser(rs.getString("id_user"));
				auditTrailModel.setPhoto(rs.getString("photo"));
				auditTrailModel.setLogin(rs.getString("login"));
				auditTrailModel.setName(rs.getString("name"));

				
				listAuditTrail.add(auditTrailModel);
			}

		

			rs.close();
			stm.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return listAuditTrail;
		
	}

}
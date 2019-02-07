package com.healthsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import com.healthsystem.database.DatabaseConnectionFactory;
import com.healthsystem.model.AuditTrailModel;
import com.healthsystem.model.ErrorLogModel;
import com.healthsystem.model.GraphModel;
import com.healthsystem.model.HealthInstitutionModel;
import com.healthsystem.model.SpecializationModel;

public class ErrorLogDAO {

	private Connection connection;

	public ErrorLogDAO() {
		this.connection = DatabaseConnectionFactory.getConnection();
	}

	public boolean add(ErrorLogModel errorLogModel) {
		 
		if (errorLogModel.getUserId() == null || errorLogModel.getUserId().toString().isEmpty()) {
			String sql = "INSERT INTO ERROR_LOG (id_error, message, name_of_file, class_name, method_name, line_number, additional_info) "
					+ " VALUES (?,?,?,?,?,?,?)";

			try {
				PreparedStatement stm = connection.prepareStatement(sql);

				stm.setString(1, errorLogModel.getIdError());
				stm.setString(2, errorLogModel.getMessage());
				stm.setString(3, errorLogModel.getNameOfFile());
				stm.setString(4, errorLogModel.getClassName());
				stm.setString(5, errorLogModel.getMethodName());
				stm.setInt(6, errorLogModel.getLineNumber());
				stm.setString(7, errorLogModel.getAdditionalInfo());

				stm.execute();
				stm.close();
				return true;

			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;
		} else {
			String sql = "INSERT INTO ERROR_LOG (id_error, message, name_of_file, class_name, method_name, line_number, additional_info, id_user) "
					+ " VALUES (?,?,?,?,?,?,?,?)";

			try {
				PreparedStatement stm = connection.prepareStatement(sql);

				stm.setString(1, errorLogModel.getIdError());
				stm.setString(2, errorLogModel.getMessage());
				stm.setString(3, errorLogModel.getNameOfFile());
				stm.setString(4, errorLogModel.getClassName());
				stm.setString(5, errorLogModel.getMethodName());
				stm.setInt(6, errorLogModel.getLineNumber());
				stm.setString(7, errorLogModel.getAdditionalInfo());
				stm.setString(8, errorLogModel.getUserId());

				stm.execute();
				stm.close();
				return true;

			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;
		}

	}

	public List<GraphModel> list() {

		List<GraphModel> listError = new ArrayList<>();

		String sql = " "+
"SELECT CONVERT(DATE,  CONVERT(datetime, SWITCHOFFSET(event_date, DATEPART(TZOFFSET, "+
"event_date AT TIME ZONE 'E. South America Standard Time')))) AS postedDate, COUNT(*) as quantity FROM ERROR_LOG WHERE  CONVERT(datetime, SWITCHOFFSET(event_date, DATEPART(TZOFFSET, "+
"event_date AT TIME ZONE 'E. South America Standard Time'))) >= ? AND  CONVERT(datetime, SWITCHOFFSET(event_date, DATEPART(TZOFFSET, "+
"event_date AT TIME ZONE 'E. South America Standard Time'))) <= ? GROUP BY CONVERT(DATE,  CONVERT(datetime, SWITCHOFFSET(event_date, DATEPART(TZOFFSET, "+
"event_date AT TIME ZONE 'E. South America Standard Time'))))";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);

			Date today = new Date();

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(today);

			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.add(Calendar.DATE, -1);

			Date lastDayOfMonth = calendar.getTime();

			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Calendar firstDay = Calendar.getInstance();
			firstDay.set(Calendar.DAY_OF_MONTH, 1);

			Date firstDayOfMonth = firstDay.getTime();

			stm.setString(1, sdf.format(firstDayOfMonth));
			stm.setString(2, sdf.format(lastDayOfMonth));

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				GraphModel errorGraphModel = new GraphModel();
				errorGraphModel.setDate(rs.getString("postedDate"));
				errorGraphModel.setQuantity(rs.getInt("quantity"));

				listError.add(errorGraphModel);
			}

			Calendar start = Calendar.getInstance();
			start.setTime(firstDayOfMonth);
			Calendar end = Calendar.getInstance();
			end.setTime(lastDayOfMonth);

			while (!start.after(end)) {
				Date targetDay = start.getTime();

				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

				 
				GraphModel graphModel = new GraphModel();
				graphModel.setDate(df.format(targetDay)); 
				if (!listError.contains(graphModel)) {
					GraphModel errorGraphModel = new GraphModel();
					errorGraphModel.setDate(df.format(targetDay));
					errorGraphModel.setQuantity(0);

					listError.add(errorGraphModel);
				}
				
				  

				start.add(Calendar.DATE, 1);
			}

			Collections.sort(listError, new Comparator<GraphModel>() {
				public int compare(GraphModel s1, GraphModel s2) {
					return s1.getDate().compareTo(s2.getDate());
				}
			});

			rs.close();
			stm.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return listError;

	}
	
	public List<ErrorLogModel> list(String startDate) {

		List<ErrorLogModel> listError = new ArrayList<>();
		String sql = "SELECT E.additional_info, E.class_name,  CONVERT(datetime, SWITCHOFFSET(e.event_date, DATEPART(TZOFFSET, e.event_date AT TIME ZONE 'E. South America Standard Time'))) AS event_date, e.id_error, e.id_user, e.line_number, e.message, e.method_name, e.name_of_file, u.id_user, u.login, u.name, u.photo FROM ERROR_LOG AS E LEFT JOIN USER_HEALTH AS U ON U.id_user = E.id_user WHERE CONVERT(DATE,  CONVERT(datetime, SWITCHOFFSET(e.event_date, DATEPART(TZOFFSET, event_date AT TIME ZONE 'E. South America Standard Time'))))= ?;";
		try {
			PreparedStatement stm = connection.prepareStatement(sql);
   
			stm.setString(1, startDate);
			
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				ErrorLogModel errorLogModel = new ErrorLogModel();
				
				errorLogModel.setIdError(rs.getString("id_error"));
				errorLogModel.setEventDate(rs.getString("event_date"));
				errorLogModel.setMessage(rs.getString("message"));
				errorLogModel.setNameOfFile(rs.getString("name_of_file"));
				errorLogModel.setClassName(rs.getString("class_name"));
				errorLogModel.setMethodName(rs.getString("method_name"));
				errorLogModel.setLineNumber(rs.getInt("line_number"));
				errorLogModel.setAdditionalInfo(rs.getString("additional_info"));
				errorLogModel.setUserId(rs.getString("id_user"));
				errorLogModel.setPhoto(rs.getString("photo"));
				errorLogModel.setLogin(rs.getString("login"));
				errorLogModel.setName(rs.getString("name"));

				
				listError.add(errorLogModel);
			}

			rs.close();
			stm.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return listError;

	}

}

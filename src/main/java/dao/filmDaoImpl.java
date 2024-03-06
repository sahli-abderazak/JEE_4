package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import metier.SingletonConnection;
import metier.Film;


public class filmDaoImpl implements lFilmDao {

	@Override
	public Film save(Film F) {
		 Connection conn=SingletonConnection.getConnection();
	       try {
			PreparedStatement ps= conn.prepareStatement("INSERT INTO FILMS(NOM_FILMS,RATE_FILMS,GENRE) VALUES(?,?,?)");
			ps.setString(1,F.getNomFilm());
			ps.setFloat(2, F.getRateFilm());
			ps.setString(3,F.getGenreFilm());
			ps.executeUpdate();
			
			
			PreparedStatement ps2= conn.prepareStatement
					("SELECT MAX(ID_FILMS) as MAX_ID FROM FILMS");
			ResultSet rs =ps2.executeQuery();
			if (rs.next()) {
				F.setIdFilm(rs.getLong("MAX_ID"));
			}
			ps.close();
			ps2.close();
				 
					
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return F;
	}

	@Override
	public List<Film> filmsParMC(String mc) {
	      List<Film> films= new ArrayList<Film>();
	       Connection conn=SingletonConnection.getConnection();
	       try {
			PreparedStatement ps= conn.prepareStatement("select * from FILMS where NOM_FILMS LIKE ?");
			ps.setString(1, "%"+mc+"%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Film f = new Film();
				f.setIdFilm(rs.getLong("ID_FILMS"));
				f.setNomFilm(rs.getString("NOM_FILMS"));
				f.setRateFilm(rs.getFloat("RATE_FILMS"));
				f.setGenreFilm(rs.getString("GENRE"));
			
				films.add(f);								
			}
				
		} catch (SQLException e) {
			
			e.printStackTrace();
		}

			return films;
	}

	@Override
	public Film getFilm(Long id) {
		    
		   Connection conn=SingletonConnection.getConnection();
		    Film f = new Film();
	       try {
			PreparedStatement ps= conn.prepareStatement("select * from FILMS where ID_FILMS = ?");
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if  (rs.next()) {
				
				f.setIdFilm(rs.getLong("ID_FILMS"));
				f.setNomFilm(rs.getString("NOM_FILMS"));
				f.setRateFilm(rs.getFloat("RATE_FILMS"));
				f.setGenreFilm(rs.getString("GENRE"));
			}
				
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
			return f;
	}

	@Override
	public Film updateFilm(Film f) {
		Connection conn=SingletonConnection.getConnection();
	       try {
			PreparedStatement ps= conn.prepareStatement("UPDATE FILMS SET NOM_FILMS=?,RATE_FILMS=? ,GENRE=? WHERE ID_FILMS=?");
			ps.setString(1,f.getNomFilm());
			ps.setFloat(2,f.getRateFilm());
			ps.setLong(4,f.getIdFilm());
			ps.setString(3,f.getGenreFilm());
			ps.executeUpdate();
			ps.close();
					
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return f;
	}

	@Override
	public void deleteFilm(Long id) {
		 Connection conn=SingletonConnection.getConnection();
	       try {
			PreparedStatement ps= conn.prepareStatement("DELETE FROM FILMS WHERE ID_FILMS = ?");
			ps.setLong(1,id);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}



}
package com.example.demo.batch.setter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import com.example.demo.batch.persistence.domain.Tutorial;

public class TutorialPreparedStatementSetter implements ItemPreparedStatementSetter<Tutorial> {

	@Override
	public void setValues(Tutorial item, PreparedStatement preparedStatement) throws SQLException {
		
		
		preparedStatement.setLong(1, item.getId());
        preparedStatement.setString(2, item.getTitle());
        preparedStatement.setString(3, item.getDescription());
        preparedStatement.setBoolean(4, item.isPublished());
	}

}

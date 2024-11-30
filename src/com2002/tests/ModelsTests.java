package com2002.tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import com2002.models.Address;
import com2002.models.Appointment;
import com2002.models.AppointmentType;
import com2002.models.DBQueries;
import com2002.models.Doctor;
import com2002.models.HealthPlan;
import com2002.models.Patient;
import com2002.models.Role;
import com2002.models.Schedule;
import com2002.models.Secretary;
import com2002.models.Staff;
import com2002.models.Usage;
import com2002.utils.Database;
import com2002.utils.DatabaseTables;

public class ModelsTests {

	// drops and creates tables again
	@BeforeClass
	public static void setUpBeforeClass() {
		try {
			DatabaseTables.setup();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("JUnit setup failed.");
		}
	}

	// drops and creates tables again
	@AfterClass
	public static void tearDownAfterClass() {
		try {
			DatabaseTables.setup();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("JUnit tear down failed.");
		}
	}

	// clears the necessary tables before each test.
	// **Must add call to delete entries for any table you use in your tests**
	@Before
	public void clearTables() {
		try {
			DBQueries.execUpdate("DELETE FROM AppointmentTreatment");
			DBQueries.execUpdate("DELETE FROM Treatments");
			DBQueries.execUpdate("DELETE FROM Appointments");
			DBQueries.execUpdate("DELETE FROM Employees");
			DBQueries.execUpdate("DELETE FROM PatientHealthPlan");
			DBQueries.execUpdate("DELETE FROM Patients");
			DBQueries.execUpdate("DELETE FROM Address");
			DBQueries.execUpdate("DELETE FROM AppointmentTypes");
			DBQueries.execUpdate("DELETE FROM HealthPlans");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("JUnit clear tables failed.");
		}
	}

	// tests constructors for creating new type of each staff member
	@Test
	public void staffConstructNew() {
		try {
			Staff secretary = new Secretary("Arthur", "Granacher", "secretary", "password");
			Staff dentist = new Doctor("Arthur", "Granacher", "dentist", "password", Role.DENTIST);
			Staff hygienist = new Doctor("Arthur", "Granacher", "hygienist", "password", Role.HYGIENIST);
			assertTrue("First name set to " + secretary.getFirstName() + ", should be Arthur.",
					secretary.getFirstName().equals("Arthur"));
			assertTrue("Last name set to " + secretary.getLastName() + ", should be Granacher.",
					secretary.getLastName().equals("Granacher"));
			assertTrue("Username set to " + secretary.getUsername() + ", should be ayjee.",
					secretary.getUsername().equals("secretary"));
			assertTrue("Role of Secretary was set to " + secretary.getRole() + ".",
					secretary.getRole().equals("Secretary"));
			assertTrue("Role of Dentist was set to " + dentist.getRole() + ".", dentist.getRole().equals("Dentist"));
			assertTrue("Role of Hygienist was set to " + hygienist.getRole() + ".",
					hygienist.getRole().equals("Hygienist"));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests constructor for existing entry in database
	@Test
	public void staffConstructExisting() {
		try {
			DBQueries
					.execUpdate("INSERT INTO Employees VALUES ('Arthur', 'Granacher', 'ayjee', 'password', 'Dentist')");
			Staff dentist = new Doctor("ayjee", "password");
			assertTrue("First name set to " + dentist.getFirstName() + ", should be Arthur.",
					dentist.getFirstName().equals("Arthur"));
			assertTrue("Last name set to " + dentist.getLastName() + ", should be Granacher.",
					dentist.getLastName().equals("Granacher"));
			assertTrue("Username set to " + dentist.getUsername() + ", should be ayjee.",
					dentist.getUsername().equals("ayjee"));
			assertTrue("Role of Secretary was set to " + dentist.getRole() + ".", dentist.getRole().equals("Dentist"));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests staff set methods
	@Test
	public void staffSetMethods() {
		try {
			DBQueries.execUpdate(
					"INSERT INTO Employees VALUES ('Arthur', 'Granacher', 'ayjee', 'password', 'Hygienist')");
			Staff hygienist = new Doctor("ayjee", "password");
			hygienist.setFirstName("NewFirst");
			hygienist.setLastName("NewLast");
			hygienist.setUsername("newuser");
			hygienist.setRole(Role.DENTIST);
			assertTrue("First name set to " + hygienist.getFirstName() + ", should be NewFirst.",
					hygienist.getFirstName().equals("NewFirst"));
			assertTrue("Last name set to " + hygienist.getLastName() + ", should be NewLast.",
					hygienist.getLastName().equals("NewLast"));
			assertTrue("Username set to " + hygienist.getUsername() + ", should be newuser.",
					hygienist.getUsername().equals("newuser"));
			assertTrue("Role set to " + hygienist.getRole() + ", should be Dentist.",
					hygienist.getRole().equals("Dentist"));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests constructor for creating new appointment
	@Test
	public void appointmentConstructNew() {
		try {
			DBQueries.execUpdate("INSERT INTO AppointmentTypes VALUES ('Checkup', 40)");
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			new Patient("Mr", "Nur", "Magid", LocalDate.of(1997, 05, 18), "07543867024", "57", "W5 1LF");
			new Doctor("Arthur", "Granacher", "dentist", "password", Role.DENTIST);
			Appointment aptmnt = new Appointment(Timestamp.valueOf("2017-11-13 11:10:00.0"),
					Timestamp.valueOf("2017-11-13 11:30:00.0"), "dentist", 1, "Notes", AppointmentType.CHECKUP, 1, 1);
			assertTrue("Start time set to " + aptmnt.getStartTime().toString() + ", should be 2017-11-13 11:10:00.0",
					aptmnt.getStartTime().toString().equals("2017-11-13 11:10:00.0"));
			assertTrue("End time set to " + aptmnt.getEndTime().toString() + ", should be 2017-11-13 11:30:00.0",
					aptmnt.getEndTime().toString().equals("2017-11-13 11:30:00.0"));
			assertTrue("Username set to " + aptmnt.getUsername() + ", should be dentist",
					aptmnt.getUsername().equals("dentist"));
			assertTrue("PatientID set to " + aptmnt.getPatientID() + ", should be 1", aptmnt.getPatientID() == 1);
			assertTrue("Notes set to " + aptmnt.getNotes() + ", should be Notes", aptmnt.getNotes().equals("Notes"));
			assertTrue("Appointment type set to " + aptmnt.getAppointmentType() + ", should be Checkup",
					aptmnt.getAppointmentType().equals("Checkup"));
			assertTrue("PatientID set to " + aptmnt.getCurrentAppointment() + ", should be 1",
					aptmnt.getCurrentAppointment() == 1);
			assertTrue("PatientID set to " + aptmnt.getTotalAppointments() + ", should be 1",
					aptmnt.getTotalAppointments() == 1);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests constructor for existing appointment
	@Test
	public void appointmentConstructExisting() {
		try {
			DBQueries.execUpdate("INSERT INTO AppointmentTypes VALUES ('Checkup', 40)");
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			new Patient("Mr", "Nur", "Magid", LocalDate.of(1997, 05, 18), "07543867024", "57", "W5 1LF");
			new Doctor("Arthur", "Granacher", "dentist", "password", Role.DENTIST);
			DBQueries.execUpdate("INSERT INTO Appointments VALUES('2017-11-13 11:10:00.0', "
					+ "'2017-11-13 11:30:00.0', 'dentist', 'Checkup', 1, 'Notes', 1, 1, 1)");
			Appointment aptmnt = new Appointment(Timestamp.valueOf("2017-11-13 11:10:00.0"), "dentist");
			assertTrue("Start time set to " + aptmnt.getStartTime().toString() + ", should be 2017-11-13 11:10:00.0",
					aptmnt.getStartTime().toString().equals("2017-11-13 11:10:00.0"));
			assertTrue("End time set to " + aptmnt.getEndTime().toString() + ", should be 2017-11-13 11:30:00.0",
					aptmnt.getEndTime().toString().equals("2017-11-13 11:30:00.0"));
			assertTrue("Username set to " + aptmnt.getUsername() + ", should be dentist",
					aptmnt.getUsername().equals("dentist"));
			assertTrue("PatientID set to " + aptmnt.getPatientID() + ", should be 1", aptmnt.getPatientID() == 1);
			assertTrue("Notes set to " + aptmnt.getNotes() + ", should be Notes", aptmnt.getNotes().equals("Notes"));
			assertTrue("Appointment type set to " + aptmnt.getAppointmentType() + ", should be Checkup",
					aptmnt.getAppointmentType().equals("Checkup"));
			assertTrue("Current appointments set to " + aptmnt.getCurrentAppointment() + ", should be 1",
					aptmnt.getCurrentAppointment() == 1);
			assertTrue("Total appointments set to " + aptmnt.getTotalAppointments() + ", should be 1",
					aptmnt.getTotalAppointments() == 1);
			assertTrue("Paid set to " + aptmnt.isPaid() + ", should be true", aptmnt.isPaid());

		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests that clashing appointments throw an exception
	@Test
	public void appointmentClashTest1() {
		try {
			DBQueries.execUpdate("INSERT INTO AppointmentTypes VALUES ('Checkup', 40)");
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			new Patient("Mr", "Nur", "Magid", LocalDate.of(1997, 05, 18), "07543867024", "57", "W5 1LF");
			new Doctor("Arthur", "Granacher", "dentist", "password", Role.DENTIST);
			new Appointment(Timestamp.valueOf("2017-11-13 11:10:00.0"), Timestamp.valueOf("2017-11-13 11:30:00.0"),
					"dentist", 1, "Notes", AppointmentType.CHECKUP, 1, 1);
			new Appointment(Timestamp.valueOf("2017-11-13 11:20:00.0"), Timestamp.valueOf("2017-11-13 11:25:00.0"),
					"dentist", 1, "Notes", AppointmentType.CHECKUP, 1, 1);
			fail("Integrity exception not thrown.");
		} catch (MySQLIntegrityConstraintViolationException e) {
			assertTrue(true);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests that clashing appointments throw an exception
	@Test
	public void appointmentClashTest2() {
		try {
			DBQueries.execUpdate("INSERT INTO AppointmentTypes VALUES ('Checkup', 40)");
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			new Patient("Mr", "Nur", "Magid", LocalDate.of(1997, 05, 18), "07543867024", "57", "W5 1LF");
			new Doctor("Arthur", "Granacher", "dentist", "password", Role.DENTIST);
			new Appointment(Timestamp.valueOf("2017-11-13 11:10:00.0"), Timestamp.valueOf("2017-11-13 11:30:00.0"),
					"dentist", 1, "Notes", AppointmentType.CHECKUP, 1, 1);
			new Appointment(Timestamp.valueOf("2017-11-10 11:00:00.0"), Timestamp.valueOf("2017-11-13 11:40:00.0"),
					"dentist", 1, "Notes", AppointmentType.CHECKUP, 1, 1);
			fail("Integrity exception not thrown.");
		} catch (MySQLIntegrityConstraintViolationException e) {
			assertTrue(true);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests that a patient can't see both doctors at the same time
	@Test
	public void appointmentClashTest3() {
		try {
			DBQueries.execUpdate("INSERT INTO AppointmentTypes VALUES ('Checkup', 40)");
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			new Patient("Mr", "Nur", "Magid", LocalDate.of(1997, 05, 18), "07543867024", "57", "W5 1LF");
			new Doctor("Arthur", "Granacher", "dentist", "password", Role.DENTIST);
			new Doctor("Nur", "Magid", "hygienist", "password", Role.HYGIENIST);
			new Appointment(Timestamp.valueOf("2017-11-13 11:10:00.0"), Timestamp.valueOf("2017-11-13 11:30:00.0"),
					"hygienist", 1, "Notes", AppointmentType.CHECKUP, 1, 1);
			new Appointment(Timestamp.valueOf("2017-11-13 11:20:00.0"), Timestamp.valueOf("2017-11-13 11:25:00.0"),
					"dentist", 1, "Notes", AppointmentType.CHECKUP, 1, 1);
			fail("Integrity exception not thrown.");
		} catch (MySQLIntegrityConstraintViolationException e) {
			assertTrue(true);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests that clashing appointments throw an exception
	@Test
	public void appointmentNoClashTest() {
		try {
			DBQueries.execUpdate("INSERT INTO AppointmentTypes VALUES ('Checkup', 40)");
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			new Patient("Mr", "Nur", "Magid", LocalDate.of(1997, 05, 18), "07543867024", "57", "W5 1LF");
			new Doctor("Arthur", "Granacher", "dentist", "password", Role.DENTIST);
			new Appointment(Timestamp.valueOf("2017-11-13 11:10:00.0"), Timestamp.valueOf("2017-11-13 11:30:00.0"),
					"dentist", 1, "Notes", AppointmentType.CHECKUP, 1, 1);
			new Appointment(Timestamp.valueOf("2017-11-13 11:30:00.0"), Timestamp.valueOf("2017-11-13 11:40:00.0"),
					"dentist", 1, "Notes", AppointmentType.CHECKUP, 1, 1);
			assertTrue(true);
		} catch (MySQLIntegrityConstraintViolationException e) {
			fail("Integrity exception thrown when it shouldn't be.");
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests removal of appointment from database and that instance variables set to
	// null
	@Test
	public void removeAppointment() throws SQLException {
		Connection conn = Database.getConnection();
		try {
			DBQueries.execUpdate("INSERT INTO AppointmentTypes VALUES ('Checkup', 40)");
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			new Patient("Mr", "Nur", "Magid", LocalDate.of(1997, 05, 18), "07543867024", "57", "W5 1LF");
			new Doctor("Arthur", "Granacher", "dentist", "password", Role.DENTIST);
			Appointment aptmnt = new Appointment(Timestamp.valueOf("2017-11-13 11:10:00.0"),
					Timestamp.valueOf("2017-11-13 11:30:00.0"), "dentist", 1, "Notes", AppointmentType.CHECKUP, 1, 1);
			aptmnt.removeAppointment();

			ResultSet rs = DBQueries.execQuery("SELECT * FROM Appointments", conn);
			if (rs.next()) {
				fail("Appointment still exists in database.");
			} else {
				assertTrue("Username set to " + aptmnt.getUsername() + ", should be null.",
						aptmnt.getUsername() == null);
				assertTrue("Start time set to " + aptmnt.getStartTime() + ", should be null.",
						aptmnt.getStartTime() == null);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		} finally {
			conn.close();
		}
	}

	@Test
	public void appointmentSetMethods() throws SQLException {
		Connection conn = Database.getConnection();
		try {
			DBQueries.execUpdate("INSERT INTO AppointmentTypes VALUES ('Checkup', 40)");
			DBQueries.execUpdate("INSERT INTO AppointmentTypes VALUES ('Cleaning', 60)");
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			new Patient("Mr", "Nur", "Magid", LocalDate.of(1997, 05, 18), "07543867024", "57", "W5 1LF");
			new Patient("Mr", "Andy", "Abc", LocalDate.of(1997, 05, 19), "07543867025", "57", "W5 1LF");
			new Doctor("Arthur", "Granacher", "dentist", "password", Role.DENTIST);
			new Doctor("John", "Ayad", "hygienist", "password", Role.HYGIENIST);
			Appointment aptmnt = new Appointment(Timestamp.valueOf("2017-11-13 11:10:00.0"),
					Timestamp.valueOf("2017-11-13 11:30:00.0"), "dentist", 1, "Notes", AppointmentType.CHECKUP, 1, 1);
			aptmnt.setAppointmentType(AppointmentType.CLEANING);
			aptmnt.setCurrentAppointment(2);
			aptmnt.setNotes("New notes");
			aptmnt.setPatientID(2);
			aptmnt.setStartEndTime(Timestamp.valueOf("2017-11-13 11:20:00.0"),
					Timestamp.valueOf("2017-11-13 11:40:00.0"));
			aptmnt.setTotalAppointments(3);
			aptmnt.setUsername("hygienist");

			ResultSet rs = DBQueries.execQuery("SELECT * FROM Appointments", conn);
			if (rs.next()) {
				assertTrue(
						"Start time set to " + aptmnt.getStartTime().toString() + ", should be 2017-11-13 11:20:00.0",
						aptmnt.getStartTime().toString().equals("2017-11-13 11:20:00.0"));
				assertTrue("End time set to " + aptmnt.getEndTime().toString() + ", should be 2017-11-13 11:40:00.0",
						aptmnt.getEndTime().toString().equals("2017-11-13 11:40:00.0"));
				assertTrue("Username set to " + aptmnt.getUsername() + ", should be hygienist",
						aptmnt.getUsername().equals("hygienist"));
				assertTrue("PatientID set to " + aptmnt.getPatientID() + ", should be 2", aptmnt.getPatientID() == 2);
				assertTrue("Notes set to " + aptmnt.getNotes() + ", should be New notes",
						aptmnt.getNotes().equals("New notes"));
				assertTrue("Appointment type set to " + aptmnt.getAppointmentType() + ", should be Cleaning",
						aptmnt.getAppointmentType().equals("Cleaning"));
				assertTrue("Current appointment set to " + aptmnt.getCurrentAppointment() + ", should be 2",
						aptmnt.getCurrentAppointment() == 2);
				assertTrue("Total appointments set to " + aptmnt.getTotalAppointments() + ", should be 3",
						aptmnt.getTotalAppointments() == 3);
			} else {
				fail("Appointment still exists in database.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		} finally {
			conn.close();
		}
	}

	// tests cost of a remedial appointment
	@Test
	public void appointmentRemedialCost() {
		try {
			DBQueries.execUpdate("INSERT INTO AppointmentTypes VALUES ('Remedial', 0)");
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			new Patient("Mr", "Nur", "Magid", LocalDate.of(1997, 05, 18), "07543867024", "57", "W5 1LF");
			new Doctor("Arthur", "Granacher", "dentist", "password", Role.DENTIST);
			Appointment aptmnt = new Appointment(Timestamp.valueOf("2017-11-13 11:10:00.0"),
					Timestamp.valueOf("2017-11-13 11:30:00.0"), "dentist", 1, "Notes", AppointmentType.REMEDIAL, 1, 1);
			DBQueries.execUpdate("INSERT INTO Treatments VALUES ('gold crown fitting', 500)");
			DBQueries.execUpdate("INSERT INTO Treatments VALUES ('silver amalgam filling', 90)");
			DBQueries.execUpdate(
					"INSERT INTO AppointmentTreatment VALUES ('2017-11-13 11:10:00.0', 'dentist', 'gold crown fitting')");
			DBQueries.execUpdate(
					"INSERT INTO AppointmentTreatment VALUES ('2017-11-13 11:10:00.0', 'dentist', 'silver amalgam filling')");
			assertTrue("Remedial cost set to " + aptmnt.calculateCost() + ", should be 590",
					aptmnt.calculateCost() == 590.0);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests cost of a cleaning appointment
	@Test
	public void appointmentCleaningCost() {
		try {
			DBQueries.execUpdate("INSERT INTO AppointmentTypes VALUES ('Cleaning', 90)");
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			new Patient("Mr", "Nur", "Magid", LocalDate.of(1997, 05, 18), "07543867024", "57", "W5 1LF");
			new Doctor("Arthur", "Granacher", "dentist", "password", Role.DENTIST);
			Appointment aptmnt = new Appointment(Timestamp.valueOf("2017-11-13 11:10:00.0"),
					Timestamp.valueOf("2017-11-13 11:30:00.0"), "dentist", 1, "Notes", AppointmentType.CLEANING, 1, 1);
			assertTrue("Cleaning cost set to " + aptmnt.calculateCost() + ", should be 90",
					aptmnt.calculateCost() == 90.0);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests cost of a checkup appointment
	@Test
	public void appointmentCheckupCost() {
		try {
			DBQueries.execUpdate("INSERT INTO AppointmentTypes VALUES ('Checkup', 45)");
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			new Patient("Mr", "Nur", "Magid", LocalDate.of(1997, 05, 18), "07543867024", "57", "W5 1LF");
			new Doctor("Arthur", "Granacher", "dentist", "password", Role.DENTIST);
			Appointment aptmnt = new Appointment(Timestamp.valueOf("2017-11-13 11:10:00.0"),
					Timestamp.valueOf("2017-11-13 11:30:00.0"), "dentist", 1, "Notes", AppointmentType.CHECKUP, 1, 1);
			assertTrue("Appointment checkup cost set to " + aptmnt.calculateCost() + ", should be 45",
					aptmnt.calculateCost() == 45.0);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests payment of appointment
	@Test
	public void appointmentPay() {
		try {
			DBQueries.execUpdate("INSERT INTO AppointmentTypes VALUES ('Checkup', 45)");
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			new Patient("Mr", "Nur", "Magid", LocalDate.of(1997, 05, 18), "07543867024", "57", "W5 1LF");
			new Doctor("Arthur", "Granacher", "dentist", "password", Role.DENTIST);
			Appointment aptmnt = new Appointment(Timestamp.valueOf("2017-11-13 11:10:00.0"),
					Timestamp.valueOf("2017-11-13 11:30:00.0"), "dentist", 1, "Notes", AppointmentType.CHECKUP, 1, 1);
			assertTrue("Appointment paid when created set to " + aptmnt.isPaid() + ", should be false",
					!aptmnt.isPaid());
			aptmnt.pay();
			assertTrue("Appointment paid set to " + aptmnt.isPaid() + ", should be true", aptmnt.isPaid());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests constructors for creating new type of address member
	@Test
	public void addressConstructNew() {
		try {
			Address address1 = new Address("57", "Mulgrave road", "Middlesex", "London", "W5 1LF");
			assertTrue("House number set to " + address1.getHouseNumber() + ", should be 57.",
					address1.getHouseNumber().equals("57"));
			assertTrue("Street name set to " + address1.getStreetName() + ", should be Mulgrave road.",
					address1.getStreetName().equals("Mulgrave road"));
			assertTrue("District set to " + address1.getDistrict() + ", should be middlesex.",
					address1.getDistrict().equals("Middlesex"));
			assertTrue("City set to " + address1.getCity() + ", should be London.",
					address1.getCity().equals("London"));
			assertTrue("Postcode set to " + address1.getPostcode() + ", should be London.",
					address1.getPostcode().equals("W5 1LF"));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests constructor for searching existing entry in address
	@Test
	public void addressConstructExisting() {
		try {
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave Road', 'Middlesex', 'London', 'W5 1LF')");
			Address addressD = new Address("57", "W5 1LF");
			assertTrue("House number set to " + addressD.getHouseNumber() + ", should be 57.",
					addressD.getHouseNumber().equals("57"));
			assertTrue("Street name set to " + addressD.getStreetName() + ", should be Mulgrave Road.",
					addressD.getStreetName().equals("Mulgrave Road"));
			assertTrue("District set to " + addressD.getDistrict() + ", should be Middlesex.",
					addressD.getDistrict().equals("Middlesex"));
			assertTrue("City set to " + addressD.getCity() + ", should be London.",
					addressD.getCity().equals("London"));
			assertTrue("Postcode set to " + addressD.getPostcode() + ", should be W5 1LF.",
					addressD.getPostcode().equals("W5 1LF"));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests address set methods
	@Test
	public void addressSetMethods() {
		try {
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			Address addressDS = new Address("57", "W5 1LF");
			addressDS.setHouseNumber("67");
			addressDS.setStreetName("Lynwood Road");
			addressDS.setDistrict("South Yorkshire");
			addressDS.setCity("Sheffield");
			addressDS.setPostcode("S10 3AN");
			assertTrue("House number set to " + addressDS.getHouseNumber() + ", should be 67.",
					addressDS.getHouseNumber().equals("67"));
			assertTrue("Street name set to " + addressDS.getStreetName() + ", should be Lynwood Road.",
					addressDS.getStreetName().equals("Lynwood Road"));
			assertTrue("District set to " + addressDS.getDistrict() + ", should be South Yorkshire.",
					addressDS.getDistrict().equals("South Yorkshire"));
			assertTrue("City set to " + addressDS.getCity() + ", should be Sheffield.",
					addressDS.getCity().equals("Sheffield"));
			assertTrue("Postcode set to " + addressDS.getPostcode() + ", should be S10 3AN.",
					addressDS.getPostcode().equals("S10 3AN"));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests patients delete patient method
	@Test
	public void deleteAddressMethod() {
		try {
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			Address.deleteAddress("57", "W5 1LF");
			Address addressDS = new Address("57", "W5 1LF");
			assertTrue("Date set to " + addressDS.getHouseNumber() + ", should be null.",
					addressDS.getHouseNumber() == null);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests constructors for creating new type of a Health Plan
	@Test
	public void healthPlanConstructNew() {
		try {
			HealthPlan healthPlan1 = new HealthPlan("NHS free plan", 0.00, 2, 2, 6);
			assertTrue("HealthPlan name set to " + healthPlan1.getName() + ", should be NHS free plan.",
					healthPlan1.getName().equals("NHS free plan"));
			assertTrue("HealthPlan price set to " + healthPlan1.getPrice() + ", should be 0.00.",
					healthPlan1.getPrice().equals(0.00));
			assertTrue("HealthPlan check up level set to " + healthPlan1.getCheckUpLevel() + ", should be 2.",
					healthPlan1.getCheckUpLevel() == 2);
			assertTrue("HealthPlan hygiene level set to " + healthPlan1.getHygieneLevel() + ", should be 2.",
					healthPlan1.getHygieneLevel() == 2);
			assertTrue("HealthPlan repair level set to " + healthPlan1.getRepairLevel() + ", should be 6.",
					healthPlan1.getRepairLevel() == 6);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests constructor for searching existing health plan
	@Test
	public void healthPlanConstructExisting() {
		try {
			DBQueries.execUpdate("INSERT INTO HealthPlans VALUES ('NHS free plan', 0.00, 2, 2, 6)");
			HealthPlan healthPlanD = new HealthPlan("NHS free plan");
			assertTrue("HealthPlan name set to " + healthPlanD.getName() + ", should be NHS free plan.",
					healthPlanD.getName().equals("NHS free plan"));
			assertTrue("HealthPlan price set to " + healthPlanD.getPrice() + ", should be 0.00.",
					healthPlanD.getPrice().equals(0.00));
			assertTrue("HealthPlan check up level set to " + healthPlanD.getCheckUpLevel() + ", should be 2.",
					healthPlanD.getCheckUpLevel() == 2);
			assertTrue("HealthPlan hygiene level set to " + healthPlanD.getHygieneLevel() + ", should be 2.",
					healthPlanD.getHygieneLevel() == 2);
			assertTrue("HealthPlan repair level set to " + healthPlanD.getRepairLevel() + ", should be 6.",
					healthPlanD.getRepairLevel() == 6);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests HealthPlan set methods
	@Test
	public void healthPlanSetMethods() {
		try {
			DBQueries.execUpdate("INSERT INTO HealthPlans VALUES ('NHS free plan', 0.00, 2, 2, 6)");
			HealthPlan healthPlanDS = new HealthPlan("NHS free plan");
			healthPlanDS.setName("The maintenance plan");
			healthPlanDS.setPrice(15.00);
			healthPlanDS.setCheckUpLevel(2);
			healthPlanDS.setHygieneLevel(2);
			healthPlanDS.setRepairLevel(0);
			assertTrue("HealthPlan name set to " + healthPlanDS.getName() + ", should be The maintenance plan.",
					healthPlanDS.getName().equals("The maintenance plan"));
			assertTrue("HealthPlan price set to " + healthPlanDS.getPrice() + ", should be 15.00.",
					healthPlanDS.getPrice().equals(15.00));
			assertTrue("HealthPlan check up level set to " + healthPlanDS.getCheckUpLevel() + ", should be 2.",
					healthPlanDS.getCheckUpLevel() == 2);
			assertTrue("HealthPlan hygiene level set to " + healthPlanDS.getHygieneLevel() + ", should be 2.",
					healthPlanDS.getHygieneLevel() == 2);
			assertTrue("HealthPlan repair level set to " + healthPlanDS.getRepairLevel() + ", should be 0.",
					healthPlanDS.getRepairLevel() == 0);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests constructors for creating new type of Patient
	@Test
	public void patientConstructNew() {
		try {
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			Patient patient1 = new Patient("Mr", "Nur", "Magid", LocalDate.of(1997, 05, 18), "07543867024", "57",
					"W5 1LF");
			assertTrue("Patient title set to " + patient1.getTitle() + ", should be Mr.",
					patient1.getTitle().equals("Mr"));
			assertTrue("Patient name set to " + patient1.getFirstName() + ", should be Nur.",
					patient1.getFirstName().equals("Nur"));
			assertTrue("Patient name set to " + patient1.getLastName() + ", should be Magid.",
					patient1.getLastName().equals("Magid"));
			assertTrue("Patient set to " + patient1.getDateOfBirth() + ", should be 1997, 05, 18.",
					patient1.getDateOfBirth().equals(LocalDate.of(1997, 05, 18)));
			assertTrue("Patient phone number set to " + patient1.getPhoneNumber() + ", should be 07543867024.",
					patient1.getPhoneNumber().equals("07543867024"));
			assertTrue("Patient house number set to " + patient1.getHouseNumber() + ", should be 57.",
					patient1.getHouseNumber().equals("57"));
			;
			assertTrue("Patient phone number set to " + patient1.getPostcode() + ", should be W5 1LF.",
					patient1.getPostcode().equals("W5 1LF"));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests constructor for searching existing entry in address
	@Test
	public void patientConstructExisting() {
		try {
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave Road', 'Middlesex', 'London', 'W5 1LF')");
			DBQueries.execUpdate(
					"INSERT INTO Patients VALUES (1, 'Mr', 'Nur', 'Magid', '1997-05-18', '07543867024', '57', 'W5 1LF')");
			Patient patientD = new Patient("Nur", "57", "W5 1LF");
			assertTrue("Patient title set to " + patientD.getTitle() + ", should be Mr.",
					patientD.getTitle().equals("Mr"));
			assertTrue("Patient name set to " + patientD.getFirstName() + ", should be Nur.",
					patientD.getFirstName().equals("Nur"));
			assertTrue("Patient name set to " + patientD.getLastName() + ", should be Magid.",
					patientD.getLastName().equals("Magid"));
			assertTrue("Patient set to " + patientD.getDateOfBirth() + ", should be 1997, 05, 18.",
					patientD.getDateOfBirth().equals(LocalDate.of(1997, 05, 18)));
			assertTrue("Patient phone number set to " + patientD.getPhoneNumber() + ", should be 07543867024.",
					patientD.getPhoneNumber().equals("07543867024"));
			assertTrue("Patient house number set to " + patientD.getHouseNumber() + ", should be 57.",
					patientD.getHouseNumber().equals("57"));
			;
			assertTrue("Patient phone number set to " + patientD.getPostcode() + ", should be W5 1LF.",
					patientD.getPostcode().equals("W5 1LF"));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests patients set methods
	@Test
	public void patientSetMethods() {
		try {
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			DBQueries.execUpdate(
					"INSERT INTO Patients VALUES (1, 'Mr', 'Nur', 'Magid', '1997-05-18', '07543867024', '57', 'W5 1LF')");
			Patient patientDS = new Patient("Nur", "57", "W5 1LF");
			patientDS.setTitle("Dr");
			patientDS.setFirstName("Arthur");
			patientDS.setLastName("Granacher");
			patientDS.setDateOfBirth(LocalDate.of(1997, 05, 17));
			patientDS.setPhoneNumber("07543867023");
			assertTrue("Patient title set to " + patientDS.getTitle() + ", should be Dr.",
					patientDS.getTitle().equals("Dr"));
			assertTrue("First name set to " + patientDS.getFirstName() + ", should be Arthur.",
					patientDS.getFirstName().equals("Arthur"));
			assertTrue("Last name set to " + patientDS.getLastName() + ", should be Granacher.",
					patientDS.getLastName().equals("Granacher"));
			assertTrue("Date of birth set to " + patientDS.getDateOfBirth() + ", should be 1997-05-17.",
					patientDS.getDateOfBirth().equals(LocalDate.of(1997, 05, 17)));
			assertTrue("PhoneNumber set to " + patientDS.getPhoneNumber() + ", should be 07543867023.",
					patientDS.getPhoneNumber().equals("07543867023"));

		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests patients subscribe method
	@Test
	public void subscribePatientMethod() {
		try {
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			DBQueries.execUpdate(
					"INSERT INTO Patients VALUES (1, 'Mr', 'Nur', 'Magid', '1997-05-18', '07543867024', '57', 'W5 1LF')");
			DBQueries.execUpdate("INSERT INTO HealthPlans VALUES ('NHS free plan', 0.00, 2, 2, 6)");
			Patient patientDS = new Patient("Nur", "57", "W5 1LF");
			patientDS.subscribePatient("NHS free plan");
			assertTrue("Usage set to " + patientDS.getUsage().getHealthPlan().getName() + ", should be NHS free plan.",
					patientDS.getUsage().getHealthPlan().getName().equals("NHS free plan"));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests patients unsubscribe method
	@Test
	public void unsubscribePatientMethod() {
		try {
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			DBQueries.execUpdate(
					"INSERT INTO Patients VALUES (1, 'Mr', 'Nur', 'Magid', '1997-05-18', '07543867024', '57', 'W5 1LF')");
			DBQueries.execUpdate("INSERT INTO HealthPlans VALUES ('NHS free plan', 0.00, 2, 2, 6)");
			Patient patientDS = new Patient("Nur", "57", "W5 1LF");
			patientDS.subscribePatient("NHS free plan");
			patientDS.unsubscribePatient();
			assertTrue("Usage set to " + patientDS.getUsage() + ", should be null.", patientDS.getUsage() == null);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// testing incremental methods
	@Test
	public void incrementalPatientMethod() {
		try {
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			DBQueries.execUpdate(
					"INSERT INTO Patients VALUES (1, 'Mr', 'Nur', 'Magid', '1997-05-18', '07543867024', '57', 'W5 1LF')");
			DBQueries.execUpdate("INSERT INTO HealthPlans VALUES ('NHS free plan', 0.00, 2, 2, 6)");
			Patient patientDS = new Patient("Nur", "57", "W5 1LF");
			patientDS.subscribePatient("NHS free plan");
			patientDS.incrementCheckUp();
			patientDS.incrementHygiene();
			patientDS.incrementRepair();
			assertTrue("Check up used set to " + patientDS.getUsage().getCheckUpUsed() + ", should be 1.",
					patientDS.getUsage().getCheckUpUsed() == (1));
			assertTrue("Hygiene used set to " + patientDS.getUsage().getHygieneUsed() + ", should be 1.",
					patientDS.getUsage().getHygieneUsed() == (1));
			assertTrue("Repair used set to " + patientDS.getUsage().getRepairUsed() + ", should be 1.",
					patientDS.getUsage().getRepairUsed() == (1));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests patients reset subscription method
	@Test
	public void resetPatientMethod() {
		try {
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			DBQueries.execUpdate(
					"INSERT INTO Patients VALUES (1, 'Mr', 'Nur', 'Magid', '1997-05-18', '07543867024', '57', 'W5 1LF')");
			DBQueries.execUpdate("INSERT INTO HealthPlans VALUES ('NHS free plan', 0.00, 2, 2, 6)");
			Patient patientDS = new Patient("Nur", "57", "W5 1LF");
			patientDS.subscribePatient("NHS free plan");
			patientDS.getUsage().setDateJoined(LocalDate.of(2016, 11, 11));
			patientDS.resetHealthPlan();
			assertTrue("Date set to " + patientDS.getUsage().getDateJoined() + ", should be q year added on.",
					patientDS.getUsage().getDateJoined().equals(LocalDate.of(2017, 11, 11)));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests patients delete patient method
	@Test
	public void deletePatientMethod() {
		try {
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			DBQueries.execUpdate(
					"INSERT INTO Patients VALUES (1, 'Mr', 'Nur', 'Magid', '1997-05-18', '07543867024', '57', 'W5 1LF')");
			Patient.deletePatient(1);
			Patient patientDS = new Patient("Nur", "57", "W5 1LF");
			assertTrue("Date set to " + patientDS.getFirstName() + ", should be null.",
					patientDS.getFirstName() == null);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests dbqueries search method
	@Test
	public void searchMethod() {
		try {
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			DBQueries.execUpdate(
					"INSERT INTO Patients VALUES (1, 'Mr', 'Nur', 'Magid', '1997-05-18', '07543867024', '57', 'W5 1LF')");
			DBQueries.execUpdate(
					"INSERT INTO Patients VALUES (2, 'Dr', 'Arthur', 'Granacher', '1997-05-17', '07543867023', '57', 'W5 1LF')");
			String arrayTest[][] = { { "Mr", "Nur", "Magid", "1997-05-18", "07543867024", "57", "W5 1LF" },
					{ "Dr", "Arthur", "Granacher", "1997-05-17", "07543867023", "57", "W5 1LF" } };
			ArrayList<Patient> patients = DBQueries.getPatientsByAddress("57", "W5 1LF");
			for (int i = 0; i < patients.size(); i++) {
				Patient patient = patients.get(i);
				assertTrue("Patient title set to " + patient.getTitle() + ", should be " + arrayTest[i][0],
						patient.getTitle().equals(arrayTest[i][0]));
				assertTrue("Patient name set to " + patient.getFirstName() + ", should be " + arrayTest[i][1],
						patient.getFirstName().equals(arrayTest[i][1]));
				assertTrue("Patient name set to " + patient.getLastName() + ", should be " + arrayTest[i][2],
						patient.getLastName().equals(arrayTest[i][2]));
				assertTrue("Patient set to " + patient.getDateOfBirth() + ", should be " + arrayTest[i][3],
						patient.getDateOfBirth().equals(LocalDate.parse(arrayTest[i][3])));
				assertTrue("Patient phone number set to " + patient.getPhoneNumber() + ", should be " + arrayTest[i][4],
						patient.getPhoneNumber().equals(arrayTest[i][4]));
				assertTrue("Patient house number set to " + patient.getHouseNumber() + ", should be " + arrayTest[i][5],
						patient.getHouseNumber().equals(arrayTest[i][5]));
				;
				assertTrue("Patient phone number set to " + patient.getPostcode() + ", should be " + arrayTest[i][6],
						patient.getPostcode().equals(arrayTest[i][6]));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// tests constructor for creating a new usage
	@Test
	public void usageConstructExisting() {
		try {
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave Road', 'Middlesex', 'London', 'W5 1LF')");
			DBQueries.execUpdate(
					"INSERT INTO Patients VALUES (1, 'Mr', 'Nur', 'Magid', '1997-05-18', '07543867024', '57', 'W5 1LF')");
			DBQueries.execUpdate("INSERT INTO HealthPlans VALUES ('NHS free plan', 0.00, 2, 2, 6)");
			Usage usage = new Usage(1, "NHS free plan");

			Usage usage1 = new Usage(1);
			assertTrue("Usage set to " + usage1.getPatientID() + ", should be 1.", usage1.getPatientID() == (1));
			assertTrue("Usage set to " + usage1.getCheckUpUsed() + ", should be 0.", usage1.getCheckUpUsed() == (0));
			assertTrue("Usage set to " + usage1.getHygieneUsed() + ", should be 0.", usage1.getHygieneUsed() == (0));
			assertTrue("Usage set to " + usage1.getRepairUsed() + ", should be 0.", usage1.getRepairUsed() == (0));
			assertTrue("Usage set to " + usage1.getDateJoined() + ", should be Todays date.",
					usage1.getDateJoined().equals(LocalDate.now()));
			assertTrue("Usage set to " + usage1.getPaymentsIssued() + ", should be 0.",
					usage1.getPaymentsIssued() == (0));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests constructors for searching usage
	@Test
	public void usageConstructNew() {
		try {
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave Road', 'Middlesex', 'London', 'W5 1LF')");
			DBQueries.execUpdate(
					"INSERT INTO Patients VALUES (1,'Mr', 'Nur', 'Magid', '1997-05-18', '07543867024', '57', 'W5 1LF')");
			DBQueries.execUpdate("INSERT INTO HealthPlans VALUES ('NHS free plan', 0.00, 2, 2, 6)");
			Usage usageD = new Usage(1, "NHS free plan");
			assertTrue("Usage set to " + usageD.getPatientID() + ", should be 1.", usageD.getPatientID() == (1));
			assertTrue("Usage set to " + usageD.getCheckUpUsed() + ", should be 0.", usageD.getCheckUpUsed() == (0));
			assertTrue("Usage set to " + usageD.getHygieneUsed() + ", should be 0.", usageD.getHygieneUsed() == (0));
			assertTrue("Usage set to " + usageD.getRepairUsed() + ", should be 0.", usageD.getRepairUsed() == (0));
			assertTrue("Usage set to " + usageD.getDateJoined() + ", should be Todays date.",
					usageD.getDateJoined().equals(LocalDate.now()));
			assertTrue("Usage set to " + usageD.getPaymentsIssued() + ", should be 0.",
					usageD.getPaymentsIssued() == (0));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests patients set methods
	@Test
	public void usageSetMethods() {
		try {
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			DBQueries.execUpdate(
					"INSERT INTO Patients VALUES (1, 'Mr', 'Nur', 'Magid', '1997-05-18', '07543867024', '57', 'W5 1LF')");
			DBQueries.execUpdate("INSERT INTO HealthPlans VALUES ('NHS free plan', 0.00, 2, 2, 6)");
			Usage usageDS = new Usage(1, "NHS free plan");
			usageDS.setCheckUpUsed(1);
			usageDS.setHygieneUsed(2);
			usageDS.setRepairUsed(3);
			usageDS.setDateJoined(LocalDate.of(2017, 10, 18));
			usageDS.setPaymentsIssued(1);
			assertTrue("Usage set to " + usageDS.getPatientID() + ", should be 1.", usageDS.getPatientID() == (1));
			assertTrue("Usage set to " + usageDS.getCheckUpUsed() + ", should be 1.", usageDS.getCheckUpUsed() == (1));
			assertTrue("Usage set to " + usageDS.getHygieneUsed() + ", should be 2.", usageDS.getHygieneUsed() == (2));
			assertTrue("Usage set to " + usageDS.getRepairUsed() + ", should be 3.", usageDS.getRepairUsed() == (3));
			assertTrue("Usage set to " + usageDS.getDateJoined() + ", should be 2017-10-18.",
					usageDS.getDateJoined().equals(LocalDate.of(2017, 10, 18)));
			assertTrue("Usage set to " + usageDS.getPaymentsIssued() + ", should be 1.",
					usageDS.getPaymentsIssued() == (1));
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests schedule for returning all appointments
	@Test
	public void testGetAppointments() {
		try {
			DBQueries.execUpdate("INSERT INTO AppointmentTypes VALUES ('Checkup', 40)");
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			new Patient("Mr", "Nur", "Magid", LocalDate.of(1997, 05, 18), "07543867024", "57", "W5 1LF");
			new Doctor("Arthur", "Granacher", "dentist", "password", Role.DENTIST);
			new Appointment(Timestamp.valueOf("2017-11-13 11:10:00.0"), Timestamp.valueOf("2017-11-13 11:30:00.0"),
					"dentist", 1, "Notes", AppointmentType.CHECKUP, 1, 1);
			ArrayList<Appointment> test = Schedule.getAppointments();
			assertTrue(
					"Start time set to " + test.get(0).getStartTime().toString() + ", should be 2017-11-13 11:10:00.0",
					test.get(0).getStartTime().toString().equals("2017-11-13 11:10:00.0"));
			assertTrue("End time set to " + test.get(0).getEndTime().toString() + ", should be 2017-11-13 11:30:00.0",
					test.get(0).getEndTime().toString().equals("2017-11-13 11:30:00.0"));
			assertTrue("Username set to " + test.get(0).getUsername() + ", should be dentist",
					test.get(0).getUsername().equals("dentist"));
			assertTrue("PatientID set to " + test.get(0).getPatientID() + ", should be 1",
					test.get(0).getPatientID() == 1);
			assertTrue("Notes set to " + test.get(0).getNotes() + ", should be Notes",
					test.get(0).getNotes().equals("Notes"));
			assertTrue("Appointment type set to " + test.get(0).getAppointmentType() + ", should be Checkup",
					test.get(0).getAppointmentType().equals("Checkup"));
			assertTrue("PatientID set to " + test.get(0).getCurrentAppointment() + ", should be 1",
					test.get(0).getCurrentAppointment() == 1);
			assertTrue("PatientID set to " + test.get(0).getTotalAppointments() + ", should be 1",
					test.get(0).getTotalAppointments() == 1);

		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests schedule for returning all appointments by day
	@Test
	public void testGetAppointmentsByDay() {
		try {
			DBQueries.execUpdate("INSERT INTO AppointmentTypes VALUES ('Checkup', 40)");
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			new Patient("Mr", "Nur", "Magid", LocalDate.of(1997, 05, 18), "07543867024", "57", "W5 1LF");
			new Doctor("Arthur", "Granacher", "dentist", "password", Role.DENTIST);
			new Appointment(Timestamp.valueOf("2017-11-13 11:10:00.0"), Timestamp.valueOf("2017-11-13 11:30:00.0"),
					"dentist", 1, "Notes", AppointmentType.CHECKUP, 1, 1);
			ArrayList<Appointment> test = Schedule.getAppointmentsByDay(Timestamp.valueOf("2017-11-13 11:10:00.0"));
			assertTrue(
					"Start time set to " + test.get(0).getStartTime().toString() + ", should be 2017-11-13 11:10:00.0",
					test.get(0).getStartTime().toString().equals("2017-11-13 11:10:00.0"));
			assertTrue("End time set to " + test.get(0).getEndTime().toString() + ", should be 2017-11-13 11:30:00.0",
					test.get(0).getEndTime().toString().equals("2017-11-13 11:30:00.0"));
			assertTrue("Username set to " + test.get(0).getUsername() + ", should be dentist",
					test.get(0).getUsername().equals("dentist"));
			assertTrue("PatientID set to " + test.get(0).getPatientID() + ", should be 1",
					test.get(0).getPatientID() == 1);
			assertTrue("Notes set to " + test.get(0).getNotes() + ", should be Notes",
					test.get(0).getNotes().equals("Notes"));
			assertTrue("Appointment type set to " + test.get(0).getAppointmentType() + ", should be Checkup",
					test.get(0).getAppointmentType().equals("Checkup"));
			assertTrue("PatientID set to " + test.get(0).getCurrentAppointment() + ", should be 1",
					test.get(0).getCurrentAppointment() == 1);
			assertTrue("PatientID set to " + test.get(0).getTotalAppointments() + ", should be 1",
					test.get(0).getTotalAppointments() == 1);

		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests schedule for returning all appointments by doctor
	@Test
	public void testGetAppointmentsByDoctor() {
		try {
			DBQueries.execUpdate("INSERT INTO AppointmentTypes VALUES ('Checkup', 40)");
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			new Patient("Mr", "Nur", "Magid", LocalDate.of(1997, 05, 18), "07543867024", "57", "W5 1LF");
			Doctor dentist = new Doctor("Arthur", "Granacher", "dentist", "password", Role.DENTIST);
			new Appointment(Timestamp.valueOf("2017-11-13 11:10:00.0"), Timestamp.valueOf("2017-11-13 11:30:00.0"),
					"dentist", 1, "Notes", AppointmentType.CHECKUP, 1, 1);
			ArrayList<Appointment> test = Schedule.getAppointmentsByDoctor(dentist);
			assertTrue(
					"Start time set to " + test.get(0).getStartTime().toString() + ", should be 2017-11-13 11:10:00.0",
					test.get(0).getStartTime().toString().equals("2017-11-13 11:10:00.0"));
			assertTrue("End time set to " + test.get(0).getEndTime().toString() + ", should be 2017-11-13 11:30:00.0",
					test.get(0).getEndTime().toString().equals("2017-11-13 11:30:00.0"));
			assertTrue("Username set to " + test.get(0).getUsername() + ", should be dentist",
					test.get(0).getUsername().equals("dentist"));
			assertTrue("PatientID set to " + test.get(0).getPatientID() + ", should be 1",
					test.get(0).getPatientID() == 1);
			assertTrue("Notes set to " + test.get(0).getNotes() + ", should be Notes",
					test.get(0).getNotes().equals("Notes"));
			assertTrue("Appointment type set to " + test.get(0).getAppointmentType() + ", should be Checkup",
					test.get(0).getAppointmentType().equals("Checkup"));
			assertTrue("PatientID set to " + test.get(0).getCurrentAppointment() + ", should be 1",
					test.get(0).getCurrentAppointment() == 1);
			assertTrue("PatientID set to " + test.get(0).getTotalAppointments() + ", should be 1",
					test.get(0).getTotalAppointments() == 1);

		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests schedule for returning all appointments by doctor and day
	@Test
	public void testGetAppointmentsByDoctorAndDay() {
		try {
			DBQueries.execUpdate("INSERT INTO AppointmentTypes VALUES ('Checkup', 40)");
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			new Patient("Mr", "Nur", "Magid", LocalDate.of(1997, 05, 18), "07543867024", "57", "W5 1LF");
			Doctor dentist = new Doctor("Arthur", "Granacher", "dentist", "password", Role.DENTIST);
			new Appointment(Timestamp.valueOf("2017-11-13 11:10:00.0"), Timestamp.valueOf("2017-11-13 11:30:00.0"),
					"dentist", 1, "Notes", AppointmentType.CHECKUP, 1, 1);
			ArrayList<Appointment> test = Schedule.getAppointmentsByDoctorAndDay(dentist,
					Timestamp.valueOf("2017-11-13 11:10:00.0"));
			assertTrue(
					"Start time set to " + test.get(0).getStartTime().toString() + ", should be 2017-11-13 11:10:00.0",
					test.get(0).getStartTime().toString().equals("2017-11-13 11:10:00.0"));
			assertTrue("End time set to " + test.get(0).getEndTime().toString() + ", should be 2017-11-13 11:30:00.0",
					test.get(0).getEndTime().toString().equals("2017-11-13 11:30:00.0"));
			assertTrue("Username set to " + test.get(0).getUsername() + ", should be dentist",
					test.get(0).getUsername().equals("dentist"));
			assertTrue("PatientID set to " + test.get(0).getPatientID() + ", should be 1",
					test.get(0).getPatientID() == 1);
			assertTrue("Notes set to " + test.get(0).getNotes() + ", should be Notes",
					test.get(0).getNotes().equals("Notes"));
			assertTrue("Appointment type set to " + test.get(0).getAppointmentType() + ", should be Checkup",
					test.get(0).getAppointmentType().equals("Checkup"));
			assertTrue("PatientID set to " + test.get(0).getCurrentAppointment() + ", should be 1",
					test.get(0).getCurrentAppointment() == 1);
			assertTrue("PatientID set to " + test.get(0).getTotalAppointments() + ", should be 1",
					test.get(0).getTotalAppointments() == 1);

		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests schedule for returning all appointments by patient
	@Test
	public void testGetAppointmentsByPatient() {
		try {
			DBQueries.execUpdate("INSERT INTO AppointmentTypes VALUES ('Checkup', 40)");
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			Patient Nur = new Patient("Mr", "Nur", "Magid", LocalDate.of(1997, 05, 18), "07543867024", "57", "W5 1LF");
			new Doctor("Arthur", "Granacher", "dentist", "password", Role.DENTIST);
			new Appointment(Timestamp.valueOf("2017-11-13 11:10:00.0"), Timestamp.valueOf("2017-11-13 11:30:00.0"),
					"dentist", 1, "Notes", AppointmentType.CHECKUP, 1, 1);
			ArrayList<Appointment> test = Schedule.getAppointmentsByPatient(Nur.getPatientID());
			assertTrue(
					"Start time set to " + test.get(0).getStartTime().toString() + ", should be 2017-11-13 11:10:00.0",
					test.get(0).getStartTime().toString().equals("2017-11-13 11:10:00.0"));
			assertTrue("End time set to " + test.get(0).getEndTime().toString() + ", should be 2017-11-13 11:30:00.0",
					test.get(0).getEndTime().toString().equals("2017-11-13 11:30:00.0"));
			assertTrue("Username set to " + test.get(0).getUsername() + ", should be dentist",
					test.get(0).getUsername().equals("dentist"));
			assertTrue("PatientID set to " + test.get(0).getPatientID() + ", should be 1",
					test.get(0).getPatientID() == 1);
			assertTrue("Notes set to " + test.get(0).getNotes() + ", should be Notes",
					test.get(0).getNotes().equals("Notes"));
			assertTrue("Appointment type set to " + test.get(0).getAppointmentType() + ", should be Checkup",
					test.get(0).getAppointmentType().equals("Checkup"));
			assertTrue("PatientID set to " + test.get(0).getCurrentAppointment() + ", should be 1",
					test.get(0).getCurrentAppointment() == 1);
			assertTrue("PatientID set to " + test.get(0).getTotalAppointments() + ", should be 1",
					test.get(0).getTotalAppointments() == 1);

		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests schedule for returning a doctor's appointments by patient
	@Test
	public void testGetDoctorAppointmentsByPatient() throws Exception {
		try {
			DBQueries.execUpdate("INSERT INTO AppointmentTypes VALUES ('Checkup', 40)");
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			Patient Nur = new Patient("Mr", "Nur", "Magid", LocalDate.of(1997, 05, 18), "07543867024", "57", "W5 1LF");
			Staff dentist = new Doctor("Arthur", "Granacher", "dentist", "password", Role.DENTIST);
			new Appointment(Timestamp.valueOf("2017-11-13 11:10:00.0"), Timestamp.valueOf("2017-11-13 11:30:00.0"),
					"dentist", 1, "Notes", AppointmentType.CHECKUP, 1, 1);
			ArrayList<Appointment> test = Schedule.getDoctorAppointmentsByPatient(dentist.getUsername(), Nur);
			assertTrue(
					"Start time set to " + test.get(0).getStartTime().toString() + ", should be 2017-11-13 11:10:00.0",
					test.get(0).getStartTime().toString().equals("2017-11-13 11:10:00.0"));
			assertTrue("End time set to " + test.get(0).getEndTime().toString() + ", should be 2017-11-13 11:30:00.0",
					test.get(0).getEndTime().toString().equals("2017-11-13 11:30:00.0"));
			assertTrue("Username set to " + test.get(0).getUsername() + ", should be dentist",
					test.get(0).getUsername().equals("dentist"));
			assertTrue("PatientID set to " + test.get(0).getPatientID() + ", should be 1",
					test.get(0).getPatientID() == 1);
			assertTrue("Notes set to " + test.get(0).getNotes() + ", should be Notes",
					test.get(0).getNotes().equals("Notes"));
			assertTrue("Appointment type set to " + test.get(0).getAppointmentType() + ", should be Checkup",
					test.get(0).getAppointmentType().equals("Checkup"));
			assertTrue("PatientID set to " + test.get(0).getCurrentAppointment() + ", should be 1",
					test.get(0).getCurrentAppointment() == 1);
			assertTrue("PatientID set to " + test.get(0).getTotalAppointments() + ", should be 1",
					test.get(0).getTotalAppointments() == 1);

		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests schedule for setting a new appointment
	@Test
	public void testSetAppointment() {
		try {
			DBQueries.execUpdate("INSERT INTO AppointmentTypes VALUES ('Checkup', 40)");
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			new Patient("Mr", "Nur", "Magid", LocalDate.of(1997, 05, 18), "07543867024", "57", "W5 1LF");
			new Doctor("Arthur", "Granacher", "dentist", "password", Role.DENTIST);
			Schedule.setAppointment(Timestamp.valueOf("2017-11-13 11:10:00.0"),
					Timestamp.valueOf("2017-11-13 11:30:00.0"), "dentist", 1, "Notes", AppointmentType.CHECKUP, 1, 1);
			Appointment aptmnt = new Appointment(Timestamp.valueOf("2017-11-13 11:10:00.0"), "dentist");
			assertTrue("Start time set to " + aptmnt.getStartTime().toString() + ", should be 2017-11-13 11:10:00.0",
					aptmnt.getStartTime().toString().equals("2017-11-13 11:10:00.0"));
			assertTrue("End time set to " + aptmnt.getEndTime().toString() + ", should be 2017-11-13 11:30:00.0",
					aptmnt.getEndTime().toString().equals("2017-11-13 11:30:00.0"));
			assertTrue("Username set to " + aptmnt.getUsername() + ", should be dentist",
					aptmnt.getUsername().equals("dentist"));
			assertTrue("PatientID set to " + aptmnt.getPatientID() + ", should be 1", aptmnt.getPatientID() == 1);
			assertTrue("Notes set to " + aptmnt.getNotes() + ", should be Notes", aptmnt.getNotes().equals("Notes"));
			assertTrue("Appointment type set to " + aptmnt.getAppointmentType() + ", should be Checkup",
					aptmnt.getAppointmentType().equals("Checkup"));
			assertTrue("PatientID set to " + aptmnt.getCurrentAppointment() + ", should be 1",
					aptmnt.getCurrentAppointment() == 1);
			assertTrue("PatientID set to " + aptmnt.getTotalAppointments() + ", should be 1",
					aptmnt.getTotalAppointments() == 1);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// tests schedule for deleting an appointment
	@Test
	public void testDeleteAppointment() throws SQLException {
		Connection conn = Database.getConnection();
		try {
			DBQueries.execUpdate("INSERT INTO AppointmentTypes VALUES ('Checkup', 40)");
			DBQueries.execUpdate("INSERT INTO Address VALUES ('57', 'Mulgrave road', 'Middlesex', 'London', 'W5 1LF')");
			new Patient("Mr", "Nur", "Magid", LocalDate.of(1997, 05, 18), "07543867024", "57", "W5 1LF");
			new Doctor("Arthur", "Granacher", "dentist", "password", Role.DENTIST);
			Appointment aptmnt = new Appointment(Timestamp.valueOf("2017-11-13 11:10:00.0"),
					Timestamp.valueOf("2017-11-13 11:30:00.0"), "dentist", 1, "Notes", AppointmentType.CHECKUP, 1, 1);
			Schedule.deleteAppointment(aptmnt);
			ResultSet rs = DBQueries.execQuery("SELECT * FROM Appointments", conn);
			if (rs.next()) {
				fail("Appointment still exists in database.");
			} else {
				assertTrue("Username set to " + aptmnt.getUsername() + ", should be null.",
						aptmnt.getUsername() == null);
				assertTrue("Start time set to " + aptmnt.getStartTime() + ", should be null.",
						aptmnt.getStartTime() == null);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			fail("Exception thrown: " + e.getMessage());
		} finally {
			conn.close();
		}
	}

}
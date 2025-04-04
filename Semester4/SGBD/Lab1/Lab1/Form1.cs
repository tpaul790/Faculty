using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using Microsoft.Data.SqlClient;

namespace Lab1
{
    public partial class Form1: Form
    {
        private SqlConnection sqlConnection = new SqlConnection("Server=DESKTOP-4NSQ3BK\\SQLEXPRESS;Database=CompanieCurierat;Integrated Security=true;TrustServerCertificate=True;");
        private SqlDataAdapter dataAdapter = new SqlDataAdapter();
        private DataSet dataSetParent = new DataSet(); 
        private DataSet dataSetChild = new DataSet();
        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            
        }

        private void button1_Click(object sender, EventArgs e)
        {
            if(sqlConnection.State != ConnectionState.Open)
                sqlConnection.Open();
            dataAdapter.SelectCommand = new SqlCommand("SELECT * FROM Soferi",sqlConnection);
            dataSetParent.Clear();
            dataAdapter.Fill(dataSetParent);
            dataGridView1.DataSource = dataSetParent.Tables[0];
        }

        private void button2_Click_1(object sender, EventArgs e)
        {
            if (dataGridView2.SelectedRows.Count > 0)
            {
                if (textBox2.TextLength != 0 && textBox3.TextLength != 0 && textBox4.TextLength != 0 && textBox5.TextLength != 0)
                {
                    try
                    {
                        string registrationNumber = (string)dataGridView2.SelectedRows[0].Cells[0].Value;
                        string brand = textBox2.Text;
                        int boxesCapacity = int.Parse(textBox3.Text);
                        string fuel = textBox4.Text;
                        int mileage = int.Parse(textBox5.Text);

                        SqlCommand command = new SqlCommand("UPDATE Masini SET marca = @brand, capacitateCuti = @boxesCapacity, combustibil = @fuel, kilometraj = @mileage WHERE nrInmatriculare = @registrationNr", sqlConnection);
                        command.Parameters.AddWithValue("@brand", brand);
                        command.Parameters.AddWithValue("@boxesCapacity", boxesCapacity);
                        command.Parameters.AddWithValue("@fuel", fuel);
                        command.Parameters.AddWithValue("@mileage", mileage);
                        command.Parameters.AddWithValue("@registrationNr", registrationNumber);

                        dataAdapter.UpdateCommand = command;
                        dataAdapter.UpdateCommand.ExecuteNonQuery();
                        dataSetChild.Clear();
                        dataAdapter.Fill(dataSetChild);
                        dataGridView2.DataSource = dataSetChild.Tables[0];

                        textBox1.Clear();
                        textBox2.Clear();
                        textBox3.Clear();
                        textBox4.Clear();
                        textBox5.Clear();
                        MessageBox.Show("The car with registration plate: " + registrationNumber + " succesfully updated!");
                    }catch(Exception ex)
                    {
                        MessageBox.Show(ex.Message);
                    }
                }
                else
                {
                    MessageBox.Show("You have to complete all the text fields");
                }
            }
            else
            {
                MessageBox.Show("You have to select a driver");
            }
        }

        private void button3_Click(object sender, EventArgs e)
        {
            if (dataGridView2.SelectedRows.Count > 0)
            {
                try
                {
                    string registrationNr = (string)dataGridView2.SelectedRows[0].Cells[0].Value;
                    SqlCommand command = new SqlCommand("DELETE FROM Masini WHERE nrInmatriculare = @rp", sqlConnection);
                    command.Parameters.AddWithValue("@rp", registrationNr);
                    dataAdapter.DeleteCommand = command;
                    dataAdapter.DeleteCommand.ExecuteNonQuery();
                    dataSetChild.Clear();
                    dataAdapter.Fill(dataSetChild);
                    dataGridView2.DataSource = dataSetChild.Tables[0];
                    MessageBox.Show("The car with registration plate: " + registrationNr + " succesfully deleted");
                }catch(Exception ex)
                {
                    MessageBox.Show(ex.Message);
                }
            }else
            {
                MessageBox.Show("You have to select a car");
            }
        }

        private void button4_Click(object sender, EventArgs e)
        {
            if(dataGridView1.SelectedRows.Count > 0)
            {
                if (textBox1.TextLength != 0 && textBox2.TextLength != 0 && textBox3.TextLength != 0 && textBox4.TextLength != 0 && textBox5.TextLength != 0)
                {
                    try
                    {
                        string registrationNumber = textBox1.Text;
                        string brand = textBox2.Text;
                        int boxesCapacity = int.Parse(textBox3.Text);
                        string fuel = textBox4.Text;
                        int mileage = int.Parse(textBox5.Text);
                        int idSofer = (int)dataGridView1.SelectedRows[0].Cells[0].Value;
                        SqlCommand command = new SqlCommand("INSERT INTO Masini Values(@rp,@brand,@capacity,@fuel,@mileage,@driver)", sqlConnection);
                        command.Parameters.AddWithValue("@rp", registrationNumber);
                        command.Parameters.AddWithValue("@brand", brand);
                        command.Parameters.AddWithValue("@capacity", boxesCapacity);
                        command.Parameters.AddWithValue("@fuel", fuel);
                        command.Parameters.AddWithValue("@mileage", mileage);
                        command.Parameters.AddWithValue("@driver", idSofer);

                        dataAdapter.InsertCommand = command;
                        dataAdapter.InsertCommand.ExecuteNonQuery();
                        dataSetChild.Clear();
                        dataAdapter.Fill(dataSetChild);
                        dataGridView2.DataSource = dataSetChild.Tables[0];

                        textBox1.Clear();
                        textBox2.Clear();
                        textBox3.Clear();
                        textBox4.Clear();
                        textBox5.Clear();

                        MessageBox.Show("New car succesfully added");
                    }catch(Exception ex)
                    {
                        MessageBox.Show(ex.Message);
                    }
                }
                else
                {
                    MessageBox.Show("You have to complete all the text fields");
                }
            }
            else
            {
                MessageBox.Show("You have to select a driver");
            }
        }

        private void dataGridView1_SelectionChanged(object sender, EventArgs e)
        {
            if (dataGridView1.SelectedRows.Count > 0)
            {
                try
                {
                    if (sqlConnection.State != ConnectionState.Open)
                        sqlConnection.Open();
                    DataGridViewRow row = dataGridView1.SelectedRows[0];
                    int idSofer = (int)row.Cells[0].Value;
                    SqlCommand selectCommand = new SqlCommand("SELECT * FROM Masini WHERE idSofer = @idS", sqlConnection);
                    selectCommand.Parameters.AddWithValue("@idS", idSofer);
                    dataAdapter.SelectCommand = selectCommand;
                    dataSetChild.Clear();
                    dataAdapter.Fill(dataSetChild);
                    dataGridView2.DataSource = dataSetChild.Tables[0];
                }catch(Exception ex)
                {
                    MessageBox.Show(ex.Message);
                }
            }
        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

        private void dataGridView1_CellContentClick_1(object sender, DataGridViewCellEventArgs e)
        {

        }

        private void textBox1_TextChanged(object sender, EventArgs e)
        {

        }

        private void button2_Click(object sender, EventArgs e)
        {

        }

        private void label1_Click(object sender, EventArgs e)
        {

        }

        private void label3_Click(object sender, EventArgs e)
        {

        }
    }
}

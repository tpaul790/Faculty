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
using System.Configuration;
using System.ComponentModel;
using System.Xml;
using System.Collections.Specialized;
using System.Runtime.CompilerServices;

namespace Lab1
{
    public partial class Form1: Form
    {
        public static string server = ConfigurationManager.AppSettings.Get("server");
        public static string dataBase = ConfigurationManager.AppSettings.Get("database");
        public static string parentTable = ConfigurationManager.AppSettings.Get("parentTable");
        public static string parentPK = ConfigurationManager.AppSettings.Get("parentPK");
        public static string childTable = ConfigurationManager.AppSettings.Get("childTable");
        public static string childFK = ConfigurationManager.AppSettings.Get("childFK");
        public static string connectionString = "Server=" + server + ";Database=" + dataBase + ";Integrated Security=true;TrustServerCertificate=True;";
        public static SqlConnection connection = new SqlConnection(connectionString);
        private DataSet dataSet = new DataSet();
        
        private SqlDataAdapter parentAdapter = new SqlDataAdapter();
        private SqlDataAdapter childAdapter = new SqlDataAdapter();

        private BindingSource bsParent = new BindingSource();
        private BindingSource bsChild = new BindingSource();
        public Form1()
        {
            InitializeComponent();
            
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            try
            {
                string parentSelectQuerry = ConfigurationManager.AppSettings.Get("parentSelectQuerry");
                string childSelectQuerry = ConfigurationManager.AppSettings.Get("childSelectQuerry");

                parentAdapter.SelectCommand = new SqlCommand(parentSelectQuerry, connection);
                childAdapter.SelectCommand = new SqlCommand(childSelectQuerry, connection);
                parentAdapter.Fill(dataSet, parentTable);
                childAdapter.Fill(dataSet, childTable);

                DataColumn pkColumn = dataSet.Tables[parentTable].Columns[parentPK];
                DataColumn fkColumn = dataSet.Tables[childTable].Columns[childFK];
                DataRelation relation = new DataRelation("FK_" + parentTable + "_" + childTable, pkColumn, fkColumn, true);

                dataSet.Relations.Add(relation);
                bsParent.DataSource = dataSet.Tables[parentTable];
                dataGridViewParent.DataSource = bsParent;
                bsChild.DataSource = bsParent;
                bsChild.DataMember = "FK_" + parentTable + "_" + childTable;
                dataGridViewChild.DataSource = bsChild;

                int yPosition = 45;
                string columnsStr = ConfigurationManager.AppSettings.Get("childColumns");
                string identity = ConfigurationManager.AppSettings.Get("identityPK");
                string childPK = ConfigurationManager.AppSettings.Get("childPK");
                if("False".Equals(identity))
                    columnsStr = childPK + "," + columnsStr;
                List<string> columns = new List<string>(columnsStr.Split(','));
                foreach (string col in columns)
                {
                    Label label = new Label();
                    label.AutoSize = true;
                    label.Location = new Point(0, yPosition);
                    label.Name = col + "Label";
                    label.Size = new Size(44, 16);
                    label.TabIndex = 0;
                    label.Text = col;

                    TextBox textBox = new TextBox();
                    textBox.Location = new Point(90, yPosition);
                    textBox.Name = col + "textBox";
                    textBox.Size = new Size(155, 22);
                    yPosition += 40;

                    myPanel.Controls.Add(label);
                    myPanel.Controls.Add(textBox);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void button4_Click(object sender, EventArgs e)
        {
            try
            {
                using (SqlConnection conn = new SqlConnection(connectionString))
                {
                    conn.Open();
                    string colsStr = ConfigurationManager.AppSettings.Get("childColumns");
                    string colsStrParams = ConfigurationManager.AppSettings.Get("childColumnsParameters");

                    string identity = ConfigurationManager.AppSettings.Get("identityPK");
                    string childPK = ConfigurationManager.AppSettings.Get("childPK");

                    if ("False".Equals(identity))
                    {
                        colsStr = childPK + "," + colsStr;
                        colsStrParams = "@" + childPK + "," + colsStrParams;
                    }

                    List<string> columns = new List<string>(colsStr.Split(','));
                    SqlCommand mySqlCommand = new SqlCommand("insert into " + childTable + "(" + colsStr + ") values(" + colsStrParams + ")", conn);
                    bool ok = true;
                    foreach (string col in columns)
                    {
                        TextBox textBox = (TextBox)myPanel.Controls[col + "textBox"];
                        if (textBox.Text.Length == 0)
                            ok = false;
                        mySqlCommand.Parameters.AddWithValue("@" + col, textBox.Text);
                    }
                    if (ok)
                    {
                        mySqlCommand.ExecuteNonQuery();
                        refreshData();
                        MessageBox.Show("New child succesfully added!");
                    }
                    else
                    {
                        MessageBox.Show("You have to complete all the text fields");
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }
        private void button3_Click(object sender, EventArgs e)
        {
            if (dataGridViewChild.SelectedRows.Count > 0)
            {
                try
                {
                    using (SqlConnection connection = new SqlConnection(connectionString))
                    {
                        connection.Open();
                        string id = dataGridViewChild.SelectedCells[0].Value.ToString();
                        string childPK = ConfigurationManager.AppSettings.Get("childPK");
                        SqlCommand mySqlCommand = new SqlCommand("delete from " + childTable + " where " + childPK + "=@id" ,connection);
                        mySqlCommand.Parameters.AddWithValue ("@id", id);
                        mySqlCommand.ExecuteNonQuery();
                        refreshData();
                        MessageBox.Show("The child with the id: " + id + " succesfully deleted");
                    }
                }
                catch (Exception ex)
                {
                    MessageBox.Show(ex.Message);
                }
            }
            else
            {
                MessageBox.Show("Please select a child entity");
            }
        }
        private void button2_Click(object sender, EventArgs e)
        {
            if (dataGridViewChild.SelectedRows.Count > 0)
            {
                try
                {
                    using (SqlConnection connection = new SqlConnection(connectionString))
                    {
                        connection.Open();
                        string id = dataGridViewChild.SelectedCells[0].Value.ToString();
                        string childPK = ConfigurationManager.AppSettings.Get("childPK");
                        SqlCommand mySqlCommand = new SqlCommand(ConfigurationManager.AppSettings.Get("childUpdateQuerry"), connection);
                        string colsStr = ConfigurationManager.AppSettings.Get("childColumns");
                        List<string> columns = new List<string>(colsStr.Split(','));
                        bool ok = true;
                        foreach (string col in columns)
                        {
                            TextBox textBox = (TextBox)myPanel.Controls[col + "textBox"];
                            if (textBox.Text.Length == 0)
                                ok = false;
                            mySqlCommand.Parameters.AddWithValue("@" + col, textBox.Text);
                        }
                        mySqlCommand.Parameters.AddWithValue("@" + childPK, id);
                        if (ok)
                        {
                            mySqlCommand.ExecuteNonQuery();
                            refreshData();
                            MessageBox.Show("Child succesfully updated!");
                        }
                        else
                        {
                            MessageBox.Show("You have to complete all the text fields");
                        }
                    }
                }
                catch (Exception ex)
                {
                    MessageBox.Show(ex.Message);
                }
            }
            else
            {
                MessageBox.Show("Please select a child entity");
            }
        }

        private void refreshData()
        {
            try
            {
                dataSet.Tables[childTable].Clear();
                childAdapter.Fill(dataSet, childTable);
                bsChild.ResetBindings(false);
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error in refreshData: " + ex.Message);
            }
        }

    }
}

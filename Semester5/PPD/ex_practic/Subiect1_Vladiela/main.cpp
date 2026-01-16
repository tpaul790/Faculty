#include <mpi.h>
#include <iostream>
#include <vector>
#include <fstream>
#include <iomanip>

using namespace std;

double calculeazaPolinom(double x, const vector<double>& coeficienti) {
    double rezultat = 0.0;
    for (double c : coeficienti) {
        rezultat = rezultat * x + c;
    }
    return rezultat;
}

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int world_size, world_rank;
    MPI_Comm_size(MPI_COMM_WORLD, &world_size);
    MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);

    vector<double> coeficienti;
    vector<double> input_data;
    vector<double> local_segment;
    vector<double> local_results;
    int n_total = 0;
    int n_coeffs = 0;

    if (world_rank == 0) {
        ifstream f_poly("polinom.txt");
        if (!f_poly.is_open()) {
            cerr << "[Eroare] Nu s-a putut deschide polinom.txt" << endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }
        double val;
        while (f_poly >> val) {
            coeficienti.push_back(val);
        }
        f_poly.close();
        n_coeffs = coeficienti.size();

        ifstream f_in("input.txt");
        if (!f_in.is_open()) {
            cerr << "[Eroare] Nu s-a putut deschide input.txt" << endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }
        while (f_in >> val) {
            input_data.push_back(val);
        }
        f_in.close();
        n_total = input_data.size();

        if (n_total == 0 || n_total % world_size != 0) {
            cerr << "[Eroare] Numarul de elemente (" << n_total 
                      << ") nu este divizibil cu numarul de procese (" << world_size << ")!" << endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }
        
        cout << "[Rank 0] S-au citit " << n_coeffs << " coeficienti si " << n_total << " numere de input." << endl;
    }

    MPI_Bcast(&n_coeffs, 1, MPI_INT, 0, MPI_COMM_WORLD);
    
    if (world_rank != 0) {
        coeficienti.resize(n_coeffs);
    }
    
    MPI_Bcast(coeficienti.data(), n_coeffs, MPI_DOUBLE, 0, MPI_COMM_WORLD);

    MPI_Bcast(&n_total, 1, MPI_INT, 0, MPI_COMM_WORLD);

    int chunk_size = n_total / world_size;
    local_segment.resize(chunk_size);

    MPI_Scatter(input_data.data(), chunk_size, MPI_DOUBLE,
                local_segment.data(), chunk_size, MPI_DOUBLE, 
                0, MPI_COMM_WORLD);


    local_results.resize(chunk_size);
    for (int i = 0; i < chunk_size; i++) {
        local_results[i] = calculeazaPolinom(local_segment[i], coeficienti);
    }

    vector<double> final_results;
    if (world_rank == 0) {
        final_results.resize(n_total);
    }

    MPI_Gather(local_results.data(), chunk_size, MPI_DOUBLE,
               final_results.data(), chunk_size, MPI_DOUBLE,
               0, MPI_COMM_WORLD);

    if (world_rank == 0) {
        ofstream f_out("output.txt");
        f_out << fixed << setprecision(2);

        for (size_t i = 0; i < final_results.size(); i++) {
            f_out << final_results[i];
            if (i < final_results.size() - 1) f_out << ", ";
        }
        f_out << endl;
        f_out.close();
        cout << "[Rank 0] Rezultatele au fost scrise in output.txt" << endl;
    }

    MPI_Finalize();
    return 0;
}
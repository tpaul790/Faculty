#include <mpi.h>
#include <iostream>
#include <vector>
#include <fstream>
#include <algorithm>
#include <limits>

double calculate_F(double x, double A, double B, double C, double D) {
    return A * x * x + B * x + C + D;
}

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int world_size, world_rank;
    MPI_Comm_size(MPI_COMM_WORLD, &world_size);
    MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);

    double A = 0.0, B = 0.0, C = 0.0, D = 0.0;

    int num_workers = world_size - 1;

    if (world_size < 2) {
        if (world_rank == 0) {
            std::cerr << "Eroare: Programul necesita cel putin 2 procese (1 Master + 1 Worker)." << std::endl;
        }
        MPI_Finalize();
        return 0;
    }

    if (world_rank == 0) {
        std::ifstream f_func("functie.txt");
        if (!f_func.is_open()) {
            std::cerr << "[Rank 0] Eroare: Nu s-a putut deschide functie.txt" << std::endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }
        if (!(f_func >> A >> B >> C >> D)) {
            std::cerr << "[Rank 0] Eroare: Nu s-au putut citi 4 coeficienti din functie.txt" << std::endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }
        f_func.close();

        double coeffs[4] = {A, B, C, D};
        MPI_Bcast(coeffs, 4, MPI_DOUBLE, 0, MPI_COMM_WORLD);

        std::ifstream f_in("input.txt");
        if (!f_in.is_open()) {
            std::cerr << "[Rank 0] Eroare: Nu s-a putut deschide input.txt" << std::endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        std::vector<double> all_input_data;
        double val;
        while (f_in >> val) {
            all_input_data.push_back(val);
        }
        f_in.close();

        int N = all_input_data.size();
        if (N == 0) {
            std::cerr << "[Rank 0] Eroare: Fisierul input.txt este gol." << std::endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        if (N % num_workers != 0) {
            std::cerr << "[Rank 0] Eroare: Numarul de valori (" << N << ") nu este divizibil cu numarul de workeri (" << num_workers << ")." << std::endl;
            MPI_Abort(MPI_COMM_WORLD, 1);
        }

        int chunk_size = N / num_workers;
        std::cout << "[Rank 0] N=" << N << ", Chunk size=" << chunk_size << std::endl;

        std::vector<double> final_results(N);

        std::vector<int> recv_offsets(world_size);
        for(int p=1; p < world_size; ++p) {
            recv_offsets[p] = (p - 1) * chunk_size;
        }

        int current_start_index = 0;
        int max_F_size = 0;

        for (int dest_rank = 1; dest_rank < world_size; dest_rank++) {
            double* send_ptr = all_input_data.data() + current_start_index;

            MPI_Send(send_ptr, chunk_size, MPI_DOUBLE, dest_rank, 0, MPI_COMM_WORLD);

            current_start_index += chunk_size;
            max_F_size = std::max(max_F_size, chunk_size);
        }

        std::vector<double> chunk_F_recv(chunk_size);
        double local_max_F = -std::numeric_limits<double>::infinity();
        double global_min_F = std::numeric_limits<double>::infinity();

        for (int p = 1; p < world_size; p++) {

            MPI_Recv(chunk_F_recv.data(), chunk_size, MPI_DOUBLE, p, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            int start_idx = recv_offsets[p];
            std::copy(chunk_F_recv.begin(), chunk_F_recv.begin() + chunk_size, final_results.begin() + start_idx);

            MPI_Recv(&local_max_F, 1, MPI_DOUBLE, p, 2, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            double local_min_F = 0.0;
            MPI_Recv(&local_min_F, 1, MPI_DOUBLE, p, 3, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            global_min_F = std::min(global_min_F, local_min_F);

            // Nu este cerut, dar e util sa afisam maximul tuturor valorilor F(x)
            // (Maximele vin de la workeri. Maximul final va fi maximul maximelor)
            if (p == 1) { // Initializam cu primul maxim
                local_max_F = local_max_F;
            } else {
                local_max_F = std::max(local_max_F, local_max_F);
            }
        }

        // 5. Scrie rezultatul in output.txt
        std::ofstream f_out("output.txt");
        for (double f_val : final_results) {
            f_out << f_val << "\n";
        }
        f_out.close();

        // 6. Afiseaza minimul total
        std::cout << "\n--- Rezultate Finale (Calculate de Rank 0) ---\n";
        std::cout << "Minimul tuturor valorilor F(x): " << global_min_F << std::endl;
        std::cout << "Maximul tuturor valorilor F(x): " << local_max_F << std::endl; // Afisam si Max, desi nu e cerut
        std::cout << "Valorile F(x) au fost salvate in output.txt." << std::endl;

    } else {
        // --- WORKER (Rank > 0) ---

        // 1. Primesc coeficientii
        double coeffs[4];
        MPI_Bcast(coeffs, 4, MPI_DOUBLE, 0, MPI_COMM_WORLD);
        A = coeffs[0]; B = coeffs[1]; C = coeffs[2]; D = coeffs[3];

        // Workerul isi calculeaza marimea chunk-ului
        int N_total = 0;
        int num_workers_total = world_size - 1;

        // Trebuie sa stie N-ul total pentru a calcula marimea chunk-ului
        // (Solutie simpla: presupunem ca Rank 0 trimite N-ul inainte de Send)
        // Daca nu il primim, singura metoda este MPI_PROBE inainte de Recv.

        // Pentru a evita MPI_PROBE, vom folosi o metoda mai sigura:
        // Presupunem ca rank 0 a trimis deja N inainte de bucla de distributie,
        // dar pentru simplitate si respectarea fluxului, vom folosi MPI_PROBE/MPI_GET_COUNT.

        MPI_Status status;
        MPI_Probe(0, 0, MPI_COMM_WORLD, &status); // Tag 0: Date input
        int chunk_size;
        MPI_Get_count(&status, MPI_DOUBLE, &chunk_size);

        std::vector<double> local_segment(chunk_size);
        std::vector<double> local_results(chunk_size);

        // 2. Primesc segmentul de date
        MPI_Recv(local_segment.data(), chunk_size, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        // 3. Calculeaza valorile F(x) si determina Max/Min local
        double local_max_F = -std::numeric_limits<double>::infinity();
        double local_min_F = std::numeric_limits<double>::infinity();

        for (int i = 0; i < chunk_size; i++) {
            double result = calculate_F(local_segment[i], A, B, C, D);
            local_results[i] = result;

            // Calculeaza min si max local
            local_max_F = std::max(local_max_F, result);
            local_min_F = std::min(local_min_F, result);
        }

        // 4. Trimit rezultatele inapoi la Rank 0

        // Tag 1: Valorile F(x) calculate
        MPI_Send(local_results.data(), chunk_size, MPI_DOUBLE, 0, 1, MPI_COMM_WORLD);

        // Tag 2: Maximul local
        MPI_Send(&local_max_F, 1, MPI_DOUBLE, 0, 2, MPI_COMM_WORLD);

        // Tag 3: Minimul local (pentru calculul minimului global)
        MPI_Send(&local_min_F, 1, MPI_DOUBLE, 0, 3, MPI_COMM_WORLD);
    }

    MPI_Finalize();
    return 0;
}
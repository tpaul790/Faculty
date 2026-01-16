#include <mpi.h>
#include <iostream>
#include <vector>
#include <fstream>
#include <map>
#include <cstdlib>
#include <ctime>
#include <iomanip>

void genereazaFisierInput(const std::string& filename) {
    std::ofstream fout(filename);
    if (!fout.is_open()) return;

    srand(time(NULL));
    int num_cartiere = 17;
    int masuratori_per_cartier = 59;

    for (int id = 1; id <= num_cartiere; ++id) {
        for (int j = 0; j < masuratori_per_cartier; ++j) {
            double temp = 10.0 + (double)(rand() % 200) / 10.0;
            fout << id << " " << temp << "\n";
        }
    }
    fout.close();
    std::cout << "[Info] Fisierul " << filename << " a fost generat." << std::endl;
}

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int world_size, world_rank;
    MPI_Comm_size(MPI_COMM_WORLD, &world_size);
    MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);

    std::vector<int> all_ids;
    std::vector<double> all_temps;
    int total_records = 0;

    if (world_rank == 0) {
        std::ifstream f_check("temperatures.txt");
        if (!f_check.good()) {
            genereazaFisierInput("temperatures.txt");
        }
        f_check.close();

        std::ifstream fin("temperatures.txt");
        int id;
        double temp;
        while (fin >> id >> temp) {
            all_ids.push_back(id);
            all_temps.push_back(temp);
        }
        fin.close();
        total_records = all_ids.size();

        std::cout << "[Root] S-au citit " << total_records << " inregistrari." << std::endl;
    }

    MPI_Bcast(&total_records, 1, MPI_INT, 0, MPI_COMM_WORLD);

    std::vector<int> sendcounts(world_size);
    std::vector<int> displs(world_size);

    int remainder = total_records % world_size;
    int sum = 0;
    for (int i = 0; i < world_size; i++) {
        sendcounts[i] = total_records / world_size + (i < remainder ? 1 : 0);
        displs[i] = sum;
        sum += sendcounts[i];
    }

    int local_count = sendcounts[world_rank];
    std::vector<int> local_ids(local_count);
    std::vector<double> local_temps(local_count);

    MPI_Scatterv(all_ids.data(), sendcounts.data(), displs.data(), MPI_INT,
                 local_ids.data(), local_count, MPI_INT,
                 0, MPI_COMM_WORLD);

    MPI_Scatterv(all_temps.data(), sendcounts.data(), displs.data(), MPI_DOUBLE,
                 local_temps.data(), local_count, MPI_DOUBLE,
                 0, MPI_COMM_WORLD);

    std::map<int, std::pair<double, int>> local_stats;
    double local_total_sum = 0.0;

    for (int i = 0; i < local_count; i++) {
        int id = local_ids[i];
        double t = local_temps[i];

        local_stats[id].first += t;
        local_stats[id].second++;

        local_total_sum += t;
    }

    double global_total_sum = 0.0;
    MPI_Reduce(&local_total_sum, &global_total_sum, 1, MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD);


    std::vector<int> send_dist_ids;
    std::vector<double> send_dist_sums;
    std::vector<int> send_dist_counts;

    for (auto const& [id, val] : local_stats) {
        send_dist_ids.push_back(id);
        send_dist_sums.push_back(val.first);
        send_dist_counts.push_back(val.second);
    }

    if (world_rank != 0) {
        int num_districts = send_dist_ids.size();
        MPI_Send(&num_districts, 1, MPI_INT, 0, 0, MPI_COMM_WORLD); // Tag 0: Size

        if (num_districts > 0) {
            MPI_Send(send_dist_ids.data(), num_districts, MPI_INT, 0, 1, MPI_COMM_WORLD);       // Tag 1: IDs
            MPI_Send(send_dist_sums.data(), num_districts, MPI_DOUBLE, 0, 2, MPI_COMM_WORLD);    // Tag 2: Sums
            MPI_Send(send_dist_counts.data(), num_districts, MPI_INT, 0, 3, MPI_COMM_WORLD);     // Tag 3: Counts
        }
    }
    else {
        std::map<int, std::pair<double, int>> global_stats = local_stats;

        for (int p = 1; p < world_size; p++) {
            int num_recs;
            MPI_Recv(&num_recs, 1, MPI_INT, p, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            if (num_recs > 0) {
                std::vector<int> recv_ids(num_recs);
                std::vector<double> recv_sums(num_recs);
                std::vector<int> recv_counts(num_recs);

                MPI_Recv(recv_ids.data(), num_recs, MPI_INT, p, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
                MPI_Recv(recv_sums.data(), num_recs, MPI_DOUBLE, p, 2, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
                MPI_Recv(recv_counts.data(), num_recs, MPI_INT, p, 3, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

                for (int i = 0; i < num_recs; i++) {
                    int id = recv_ids[i];
                    global_stats[id].first += recv_sums[i];
                    global_stats[id].second += recv_counts[i];
                }
            }
        }

        std::cout << "\n--- REZULTATE ANALIZA TEMPERATURA ---\n";

        double global_avg = global_total_sum / total_records;
        std::cout << "Temperatura medie a INTREGULUI ORAS: " << std::fixed << std::setprecision(2) << global_avg << " grade.\n";
        std::cout << "--------------------------------------\n";

        // 2. Media pe Cartiere
        std::cout << "Temperaturi medii per CARTIER:\n";
        for (auto const& [id, val] : global_stats) {
            double district_sum = val.first;
            int district_count = val.second;
            double district_avg = district_sum / district_count;

            std::cout << "Cartier ID " << id << ": " << district_avg << " grade (" << district_count << " masuratori)\n";
        }
    }

    MPI_Finalize();
    return 0;
}
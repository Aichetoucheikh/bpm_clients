package mr.bpm.bpm_clients.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDataDto {
    private long totalClients;
    private long clientsActifs;
    private long clientsBloques;
    private long totalEmployes;
    // La liste des employés suspendus, qui sera remplie seulement pour l'admin
    private List<EmployeModel> employesSuspendus;
}

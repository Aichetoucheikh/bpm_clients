package mr.bpm.bpm_clients.controlers;

import mr.bpm.bpm_clients.models.DashboardDataDto;
import mr.bpm.bpm_clients.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/data") // Un endpoint clair pour toutes les données du dashboard
    public ResponseEntity<DashboardDataDto> getDashboardData() {
        return ResponseEntity.ok(dashboardService.getDashboardData());
    }
}
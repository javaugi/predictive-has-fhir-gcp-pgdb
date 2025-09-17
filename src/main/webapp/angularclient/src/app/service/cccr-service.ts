// Angular Service for CCCR Dashboard
@Injectable({ providedIn: 'root' })
export class CCCRService {
  private apiUrl = environment.apiBaseUrl + '/cccr';
  
  constructor(private http: HttpClient, private authService: AuthService) {}
  
  getClaimsByStatus(status: ClaimStatus): Observable<ClinicalClaim[]> {
    return this.http.get<ClinicalClaim[]>(`${this.apiUrl}/claims`, {
      params: { status },
      headers: this.getAuthHeaders()
    });
  }
  
  processBatchClaims(claims: ClinicalClaim[]): Observable<BatchProcessResult> {
    return this.http.post<BatchProcessResult>(
      `${this.apiUrl}/process-batch`, 
      claims,
      { headers: this.getAuthHeaders() }
    );
  }
  
  private getAuthHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getToken()}`,
      'Content-Type': 'application/json'
    });
  }
}

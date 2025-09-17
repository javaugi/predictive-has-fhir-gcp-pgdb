// Component for Claim Review Interface
@Component({
  selector: 'app-claim-review',
  templateUrl: './claim-review.component.html',
  styleUrls: ['./claim-review.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ClaimReviewComponent implements OnInit {
  claims$: Observable<ClinicalClaim[]>;
  selectedClaim: ClinicalClaim;
  
  constructor(private cccrService: CCCRService, 
              private route: ActivatedRoute) {}
  
  ngOnInit() {
    this.claims$ = this.route.queryParams.pipe(
      switchMap(params => {
        const status = params['status'] || ClaimStatus.PENDING;
        return this.cccrService.getClaimsByStatus(status);
      })
    );
  }
  
  onClaimSelect(claim: ClinicalClaim): void {
    this.selectedClaim = claim;
  }
  
  approveClaim(claim: ClinicalClaim): void {
    this.cccrService.updateClaimStatus(claim.id, ClaimStatus.APPROVED)
      .subscribe(() => this.refreshClaims());
  }
}
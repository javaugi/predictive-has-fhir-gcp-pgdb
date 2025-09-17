module.exports = {
  ci: {
    collect: {
      // URL(s) to audit. This should be the URL of your deployed or locally served app.
      url: ['http://localhost:3000'], // Or your staging URL
      // Number of times to run Lighthouse for each URL
      numberOfRuns: 3,
      // Optional: Set a static asset server if running locally
      // staticDistDir: './build', // For React apps, assuming 'build' is your production output
    },
    assert: {
      // Define assertions for different categories.
      // Lighthouse scores are 0-100. Set a minimum acceptable score.
      assertions: {
        'accessibility:': ['error', { minScore: 90 }], // Fail if accessibility score is below 90
        'best-practices:': ['warn', { minScore: 80 }],  // Warn if best practices score is below 80
        'performance:': ['warn', { minScore: 70 }],    // Warn if performance score is below 70
        'seo:': ['warn', { minScore: 90 }],            // Warn if SEO score is below 90
        // You can also assert specific audit results:
        // 'aria-allowed-attr': 'off', // Turn off a specific audit if it's not relevant or you handle it differently
        // 'color-contrast': ['error', { minScore: 0.9 }], // Example for a specific audit
      },
    },
    upload: {
      // Optional: Upload results to Lighthouse CI server or a custom storage
      // target: 'lhci', // default
      // serverBaseUrl: 'http://localhost:8080', // URL of your LHCI server
      // token: 'YOUR_LHCI_TOKEN', // LHCI server upload token
      // Or save as artifacts in your CI/CD system
      target: 'filesystem',
      outputDir: './.lighthouseci',
    },
  },
};

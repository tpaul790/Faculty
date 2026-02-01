export interface Product {
    code: number;
    name: string;
    quantity: number; // Stoc scriptic (de la server)
}

// Extindem produsul pentru a include starea locală a auditului
export interface AuditItem extends Product {
    counted?: number; // Cantitatea introdusă de user
    status: 'idle' | 'sending' | 'error' | 'synced'; // Starea trimiterii către server
    errorMessage?: string;
}

export interface AuditPayload {
    code: number;
    counted: number;
    zone: string;
}
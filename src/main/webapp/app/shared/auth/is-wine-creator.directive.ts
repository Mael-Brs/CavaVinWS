import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { Principal } from './principal.service';
import { Wine } from '../../entities/wine/wine.model';

/**
* @whatItDoes Conditionally includes an HTML element if current user has any
* of the authorities passed as the `expression`.
*
* @howToUse
* ```
*     <some-element *jhiIsWineCreator="wine">...</some-element>
* ```
*/
@Directive({
    selector: '[jhiIsWineCreator]'
})
export class IsWineCreatorDirective {

    private wine: Wine;

    constructor(private principal: Principal, private templateRef: TemplateRef<any>, private viewContainerRef: ViewContainerRef) {
    }

    @Input()
    set jhiIsWineCreator(value: Wine) {
        this.wine = value;
        this.principal.identity().then((identity) => {
            this.updateView(identity);
        });
        // Get notified each time authentication state changes.
        this.principal.getAuthenticationState().subscribe((identity) => this.updateView(identity));
    }

    private updateView(identity): void {
        this.viewContainerRef.clear();
        if (identity && identity.id === this.wine.creatorId) {
            this.viewContainerRef.createEmbeddedView(this.templateRef);
        }
    }
}

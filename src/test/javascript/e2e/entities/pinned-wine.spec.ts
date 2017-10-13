import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('PinnedWine e2e test', () => {

    let navBarPage: NavBarPage;
    let pinnedWineDialogPage: PinnedWineDialogPage;
    let pinnedWineComponentsPage: PinnedWineComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);


    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load PinnedWines', () => {
        navBarPage.goToEntity('pinned-wine');
        pinnedWineComponentsPage = new PinnedWineComponentsPage();
        expect(pinnedWineComponentsPage.getTitle()).toMatch(/cavavinApp.pinnedWine.home.title/);
    });

    it('should load create PinnedWine dialog', () => {
        pinnedWineComponentsPage.clickOnCreateButton();
        pinnedWineDialogPage = new PinnedWineDialogPage();
        expect(pinnedWineDialogPage.getModalTitle()).toMatch(/cavavinApp.pinnedWine.home.createOrEditLabel/);
        pinnedWineDialogPage.close();
    });

   /* it('should create and save PinnedWines', () => {
        pinnedWineComponentsPage.clickOnCreateButton();
        pinnedWineDialogPage.wineSelectLastOption();
        pinnedWineDialogPage.save();
        expect(pinnedWineDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); */

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class PinnedWineComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-pinned-wine div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class PinnedWineDialogPage {
    modalTitle = element(by.css('h4#myPinnedWineLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    wineSelect = element(by.css('select#field_wine'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    wineSelectLastOption = function () {
        this.wineSelect.all(by.tagName('option')).last().click();
    }

    wineSelectOption = function (option) {
        this.wineSelect.sendKeys(option);
    }

    getWineSelect = function () {
        return this.wineSelect;
    }

    getWineSelectedOption = function () {
        return this.wineSelect.element(by.css('option:checked')).getText();
    }

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}

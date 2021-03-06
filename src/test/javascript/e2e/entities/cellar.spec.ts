import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Cellar e2e test', () => {

    let navBarPage: NavBarPage;
    let cellarDialogPage: CellarDialogPage;
    let cellarComponentsPage: CellarComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);


    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Cellars', () => {
        navBarPage.goToEntity('cellar');
        cellarComponentsPage = new CellarComponentsPage();
        expect(cellarComponentsPage.getTitle()).toMatch(/cavavinApp.cellar.home.title/);

    });

    it('should load create Cellar dialog', () => {
        cellarComponentsPage.clickOnCreateButton();
        cellarDialogPage = new CellarDialogPage();
        expect(cellarDialogPage.getModalTitle()).toMatch(/cavavinApp.cellar.home.createOrEditLabel/);
        cellarDialogPage.close();
    });

    it('should create and save Cellars', () => {
        cellarComponentsPage.clickOnCreateButton();
        cellarDialogPage.setCapacityInput('5');
        expect(cellarDialogPage.getCapacityInput()).toMatch('5');
        cellarDialogPage.save();
        expect(cellarDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CellarComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-cellar div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CellarDialogPage {
    modalTitle = element(by.css('h4#myCellarLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    capacityInput = element(by.css('input#field_capacity'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setCapacityInput = function (capacity) {
        this.capacityInput.sendKeys(capacity);
    }

    getCapacityInput = function () {
        return this.capacityInput.getAttribute('value');
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

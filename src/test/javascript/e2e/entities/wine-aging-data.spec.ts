import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('WineAgingData e2e test', () => {

    let navBarPage: NavBarPage;
    let wineAgingDataDialogPage: WineAgingDataDialogPage;
    let wineAgingDataComponentsPage: WineAgingDataComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load WineAgingData', () => {
        navBarPage.goToEntity('wine-aging-data');
        wineAgingDataComponentsPage = new WineAgingDataComponentsPage();
        expect(wineAgingDataComponentsPage.getTitle()).toMatch(/cavavinApp.wineAgingData.home.title/);

    });

    it('should load create WineAgingData dialog', () => {
        wineAgingDataComponentsPage.clickOnCreateButton();
        wineAgingDataDialogPage = new WineAgingDataDialogPage();
        expect(wineAgingDataDialogPage.getModalTitle()).toMatch(/cavavinApp.wineAgingData.home.createOrEditLabel/);
        wineAgingDataDialogPage.close();
    });

   /* it('should create and save WineAgingData', () => {
        wineAgingDataComponentsPage.clickOnCreateButton();
        wineAgingDataDialogPage.setMinKeepInput('5');
        expect(wineAgingDataDialogPage.getMinKeepInput()).toMatch('5');
        wineAgingDataDialogPage.setMaxKeepInput('5');
        expect(wineAgingDataDialogPage.getMaxKeepInput()).toMatch('5');
        wineAgingDataDialogPage.colorSelectLastOption();
        wineAgingDataDialogPage.regionSelectLastOption();
        wineAgingDataDialogPage.save();
        expect(wineAgingDataDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); */

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class WineAgingDataComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-wine-aging-data div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class WineAgingDataDialogPage {
    modalTitle = element(by.css('h4#myWineAgingDataLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    minKeepInput = element(by.css('input#field_minKeep'));
    maxKeepInput = element(by.css('input#field_maxKeep'));
    colorSelect = element(by.css('select#field_color'));
    regionSelect = element(by.css('select#field_region'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setMinKeepInput = function (minKeep) {
        this.minKeepInput.sendKeys(minKeep);
    }

    getMinKeepInput = function () {
        return this.minKeepInput.getAttribute('value');
    }

    setMaxKeepInput = function (maxKeep) {
        this.maxKeepInput.sendKeys(maxKeep);
    }

    getMaxKeepInput = function () {
        return this.maxKeepInput.getAttribute('value');
    }

    colorSelectLastOption = function () {
        this.colorSelect.all(by.tagName('option')).last().click();
    }

    colorSelectOption = function (option) {
        this.colorSelect.sendKeys(option);
    }

    getColorSelect = function () {
        return this.colorSelect;
    }

    getColorSelectedOption = function () {
        return this.colorSelect.element(by.css('option:checked')).getText();
    }

    regionSelectLastOption = function () {
        this.regionSelect.all(by.tagName('option')).last().click();
    }

    regionSelectOption = function (option) {
        this.regionSelect.sendKeys(option);
    }

    getRegionSelect = function () {
        return this.regionSelect;
    }

    getRegionSelectedOption = function () {
        return this.regionSelect.element(by.css('option:checked')).getText();
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

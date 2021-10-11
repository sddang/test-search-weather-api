@SearchWeather
Feature: [Open Weather Map] Search Weather

  @SearchWeather
  Scenario Outline: Search weather by City
    Given I perform search with "<value>"
    And The overall response status should be
      | message | accurate |
      | count   | 2        |
      | cod     | 200      |
    And Response city and country should be "<value>" and "<country>"
    @cityOnly
    Examples:
      | value       | country |
      | Ho Chi Minh | VN      |
    @cityAndCountryName
    Examples:
      | value                | country |
      | Ho Chi Minh, Vietnam | VN      |
    @cityAndCountryCode
    Examples:
      | value           | country |
      | Ho Chi Minh, VN | VN      |

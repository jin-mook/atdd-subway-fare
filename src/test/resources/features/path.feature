Feature: 경로 조회 관련 기능

  Background:
    Given 필요한 역과, 구간, 노선을 등록합니다.
    And 노선을 등록할 때 신분당선은 추가요금 900원을 적용합니다.
    And 노선을 등록할 때 삼호선은 추가요금 500원을 적용합니다.

  Scenario: 로그인 하지 않은 사용자가 두 역 사이의 가장 거리가 가까운 길을 조회합니다.
    When 논현역에서 양재역으로 갈 수 있는 길을 거리 기준으로 조회합니다.
    Then 논현역부터 양재역까지의 거리가 가장 빠른 길의 역들과 총 거리, 소요 시간을 응답받습니다.
    And 거리 기준 지하철 경로 조회에 이용 요금도 함께 응답합니다.

  Scenario: 로그인 하지 않은 사용자가 두 역 사이의 가장 소요 시간이 적은 길을 조회합니다.
    When 논현역에서 양재역으로 갈 수 있는 길을 소요 시간 기준으로 조회합니다.
    Then 논현역부터 양재역까지의 소요 시간이 가장 빠른 길의 역들과 총 거리, 소요 시간을 응답받습니다.
    And 소요 시간 기준 지하철 경로 조회에 이용 요금도 함께 응답합니다.

  Scenario: 서로 연결되어 있지 않는 역의 최단거리는 조회할 수 없습니다.
    When 서로 연결되어 있지 않은 역의 최단거리를 요청합니다.
    Then 연결되어 있지 않다는 에러 응답을 전달받습니다.

  Scenario: 존재하지 않는 역이라면 최단 거리를 조회할 수 없습니다.
    When 존재하지 않은 역의 최단거리를 요청합니다.
    Then 존재하지 않는 역이라는 에러 응답을 전달받습니다.

  Scenario Outline: 깃헙 로그인 한 사용자가 두 역 사이의 가장 거리가 가까운 길을 조회합니다.
    Given <age> 살 사용자가 로그인 합니다.
    When 로그인 토큰과 함께 논현역에서 양재역으로 갈 수 있는 길을 거리 기준으로 조회합니다.
    Then 논현역부터 양재역까지의 거리가 가장 빠른 길의 역들과 총 거리, 소요 시간을 응답받습니다.
    And 나이 기준으로 최종 이용 요금인 <payment> 가격도 함께 응답합니다.

    Examples:
      | age | payment |
      | 6   | 900     |
      | 13  | 1440    |
      | 19  | 2150    |


  Scenario Outline: 깃헙 로그인 한 사용자가 두 역 사이의 가장 소요 시간이 적은 길을 조회합니다.
    Given <age> 살 사용자가 로그인 합니다.
    When 로그인 토큰과 함께 논현역에서 양재역으로 갈 수 있는 길을 소요 시간 기준으로 조회합니다.
    Then 논현역부터 양재역까지의 소요 시간이 가장 빠른 길의 역들과 총 거리, 소요 시간을 응답받습니다.
    And 나이 기준으로 최종 이용 요금인 <payment> 가격도 함께 응답합니다.

    Examples:
      | age | payment |
      | 6   | 1150    |
      | 13  | 1840    |
      | 19  | 2650    |